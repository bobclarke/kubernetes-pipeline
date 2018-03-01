import com.stack1.plib.Context
import com.stack1.plib.Handlers

def call(Context context, handlers, String targetBranch) {
	def unitTests = []
	def sanityTests = []
	def integrationTests = []
	def allTests = []
	def builder
	def appDeployer
	def targetEnv="feature"
	def epoch

	String integrationEnvironment = $targetBranch

	stage("Initialize"){
		node(){
			try{
				echo "TARGET_BRANCH: ${targetBranch}"
				epoch =	sh(returnStdout: true, script: 'date +%d%m%Y%H%M').trim()
				checkout scm

				echo "Loading all handlers"
				echo "Loading Builder: ${handlers.builder}"
				builder = load("${handlers.builder}")

				echo "Loading Deployer: ${handlers.deployer}"
				appDeployer = load(handlers.deployer)

				for (String test: handlers.getUnitTests()) {
					echo "Loading ${test}"
					unitTests.add( load("${test}"))
				}
				for (String test: handlers.getStaticAnalysis()) {
					echo "Loading ${test}"
					sanityTests.add( load("${test}"))
				}
				for (String test: handlers.getIntegrationTests()) {
					echo "Loading ${test}"
					integrationTests.add( load("${test}"))
				}

				allTests.addAll(unitTests)
				allTests.addAll(sanityTests)
				allTests.addAll(integrationTests)
			} catch(error) {
				echo error.message
				throw error
			} finally{
				step([$class: 'WsCleanup', notFailBuild: true])
			}
		}
		milestone (label: 'Ready')
	}
	try {
		if(!unitTests.empty){
			stage("Unit Tests"){
				for (Object testClass: unitTests) {
					def currentTest = testClass
					currentTest.runTest(targetBranch, context)
				}
			}
			milestone (label: 'UnitTests')
		}
		if(!sanityTests.empty){
			stage("Static Analysis"){
				def codeSanitySchedule = [:]
				for (Object testClass: sanityTests) {
					def currentTest = testClass
					codeSanitySchedule[currentTest.name()] = { currentTest.runTest(targetBranch, context) }
				}
				try{
					parallel codeSanitySchedule
					milestone (label: 'StaticAnalysis')
				} catch(error) {
					echo "Static Analysis has failed."
					throw error
				} finally {
					//Make a decision
				}
			}
		}
		if(!integrationTests.empty){
			stage("Package"){
				builder.pack(targetBranch, targetEnv, context)
			}
			milestone (label: 'Build')
		}
		lock(inversePrecedence: true, quantity: 1, resource: integrationEnvironment ) {
			if(!integrationTests.empty){
				stage("Deploy"){
					appDeployer.deploy(targetBranch, context)
				}
				stage("Integration Tests"){
					def integrationTestSchedule = [:]

					for (Object testClass: integrationTests) {
						def currentTest = testClass
						integrationTestSchedule[currentTest.name()] = { currentTest.runTest(targetBranch, context) }
					}
					try{
						parallel integrationTestSchedule
						milestone (label: 'IntegrationTests')
					} catch(error) {
						echo "Integration tests failed"
						throw error
					} finally {
						//
					}
				}
			}
		}
	} catch(error) {
		echo "Mandatory Tests have failed. Aborting"
		throw error
	} finally {
		stage("Cleanup"){
			echo "Cleanup"
		}
		stage("End"){ 
			echo "End" 
		}
	}
}
return this;
