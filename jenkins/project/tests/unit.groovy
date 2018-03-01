

def run(String targetBranch, context){
    node() {
        checkout scm
        def appname = appName(context.application, targetBranch)
        def coverageStash = context.config.unit.coverage_stash_name
        try {
            withEnv([
                "appname=$appname"
            ]) {
                sh 'unit.sh'
            }
        } catch (error) {
            echo "FAILURE: Unit Tests failed"
            echo error.message
            throw error
        } finally {
            //step([$class: 'WsCleanup', notFailBuild: true])
        }
    }
}
return this;
