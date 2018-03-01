import com.stack1.plib.Context
import com.stack1.plib.Handlers

def call(String application, handlers, String configuration){
	this.call(application, handlers, configuration,	'admin@example.com', 120)
}

def call(String application,
		handlers,
		String configuration,
		String notifyList,
		Integer timeoutInMinutes){
	try {
		timeout(timeoutInMinutes){
			this.callHandler(application, handlers, configuration)
			currentBuild.result = 'SUCCESS'
		}
	} catch(error) {
		currentBuild.result = 'FAILURE'
		throw error
	} finally {
		//
	}
}

def callHandler(String application, handlers, String configuration) {
	def targetCommit
  	def branch
  	def localBranchName
	Context context
	Handlers initializer

	node (){
		checkout scm
    		localBranchName = sh(returnStdout: true, script: "git rev-parse --abbrev-ref HEAD").trim()
		targetCommit = sh(returnStdout: true, script: 'git rev-parse HEAD').trim()
		context = new Context(application, readFile(configuration))
		//step([$class: 'WsCleanup', notFailBuild: true])
	}

  	branch = env.BRANCH_NAME
  	if (branch == null) {
    		branch = localBranchName
  	}

	if (branch =~ /^feature\/[0-9]*\/[0-9]*\/[0-9]*/ ) {
		  featureWorkflow(context, handlers, targetCommit)

	} else if (branch =~ /^sprint[0-9]+\/.+$/ || branch) {
		  featureWorkflow(context, handlers, branch)

	} else if (branch =~ /^release.*$/) {
		  hawkIntegrationWorkflow(context, handlers, utils.friendlyName(branch, 40))

	} else if (branch =~ /^master$/ ) {
		  hawkIntegrationWorkflow(context, handlers, 'master')

	} else if (branch =~ /^hotfixes.*$/ ) {
		  hawkIntegrationWorkflow(context, handlers, utils.friendlyName(branch, 40))

	} else if (branch =~ /^develop$/ ) {
		  hawkIntegrationWorkflow(context, handlers, 'develop')
	} else {
      		echo "Not able to determine the type of branch $branch. Stopping."
	}
	echo "End of pieline"
}

return this;
