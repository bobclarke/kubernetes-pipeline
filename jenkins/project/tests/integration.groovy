
def run(String targetBranch, context){

    node(){
    checkout scm

        def gulpOption = context.config.gulp.bdd ?: ''
        try{
            sauce(credentialsID) {
                withEnv([
                    "appname=${appname}",
                ]) {
                    sauceconnect(useGeneratedTunnelIdentifier: true,
                        verboseLogging: true,
                        sauceConnectPath: sauceBinary,
                        options: ""
                    ) {
                        sh "integration.sh"
                    }
                }
            }
            step([
                $class: 'CucumberReportPublisher',
                failedFeaturesNumber: 99999999999,
                failedScenariosNumber: 9999999999,
                failedStepsNumber: 99999999999,
                fileExcludePattern: '',
                jsonReportDirectory: 'wdio/output',
                parallelTesting: false,
                pendingStepsNumber: 99999999999,
                skippedStepsNumber: 99999999999,
                trendsLimit: 0,
                undefinedStepsNumber: 99999999999
            ])
            dir('wdio/output') {
                stash name: stashName, includes: '*.json'
                withEnv([
                    "SCENARIO_PASS_THRESHOLD=${scenarioPassThreshold}"
                ]){
                    sh "${env.WORKSPACE}/tests/check-thresholds.sh"
                }
            }

        }catch (error) {
            echo "FAILURE: Integration tests failed"
            echo error.message
            throw error
        }finally{
            //step([$class: 'WsCleanup', notFailBuild: true])
        }
}
return this;
