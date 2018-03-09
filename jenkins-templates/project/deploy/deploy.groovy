def deploy(String targetBranch, context) {
   node(){
       withCredentials([
           usernamePassword(
               credentialsId: my_creds,
               passwordVariable: 'PASS',
               usernameVariable: 'USER'
           )
       ]) {
           withEnv([
               "DEP_HOME=${env.WORKSPACE}",
           ]) {
               try {
                    sh 'deploy.sh'
                 }
               } catch(error) {
                   echo "FAILURE: Deployment failed"
               } finally{
                 //
               }
           } 
       }
   }
}
return this;
