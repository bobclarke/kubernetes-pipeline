def pack(String targetBranch, String targetEnv, context){

  def applicationServerEnv = context.config.application.SERVER_ENV ?: 'production'

  node(){

    checkout scm
    def versionNumber = env.BUILD_NUMBER
    def packageDir = context.config.package.directory

    try {
      withEnv([
	"environment=${targetEnv}",
      ]) {
        sh 'package.sh'
      }
    } catch (error) {
      echo "FAILURE: Application Build failed"
      throw error
    } finally{
      step([
        $class: 'WsCleanup', 
        notFailBuild: true
      ])
    }
  }
}
return this;
