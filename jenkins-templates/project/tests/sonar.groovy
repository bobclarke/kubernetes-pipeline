 
def run(String targetBranch, context){
    def appname = appName(context.application, typeOfBranch)

    node() {
        try {
            checkout scm

            withSonarQubeEnv("${sonarServer}") {
                sh " rm -f ${coverageReportFile} "
                unstash  coverageStash
                sh """ source pipelines/scripts/functions && \\
                    sonar-runner -X \\
                        -Dsonar.host.url=${SONAR_HOST_URL} \\
                        -Dsonar.jdbc.url=\'${SONAR_JDBC_URL}\' \\
                        -Dsonar.jdbc.username=${SONAR_JDBC_USERNAME} \\
                        -Dsonar.jdbc.password=\'${SONAR_JDBC_PASSWORD}\' \\
                        -Dsonar.login=${SONAR_LOGIN} \\
                        -Dsonar.pasword=${SONAR_PASSWORD} \\
                        -Dsonar.projectKey=${sonarProject} \\
                        -Dsonar.projectName=${sonarProject} \\
                        -Dappname=${sonarProject} \\
                        -DbranchName=${typeOfBranch} \\
                        -Dsonar.projectVersion=${env.BUILD_NUMBER} \\
                        -Dsonar.sources=. \\
                        -Dsonar.exclusions=\'${exclusions}\' \\
                        -Dsonar.coverage.exclusions=\'${coverageExclusions}\' \\
                        -Dsonar.javascript.lcov.reportPath=${coverageReportFile} \\
                        -Dsonar.sourceEncoding=UTF-8 \\
                        -Dsonar.qualitygate=${qualityGate}\\
                        -Dsonar.scm.enabled=true \\
                        ${javaOptionOverrides}
                """
            }
        } catch (error) {
            echo error.message
            throw error
        }
        finally {
            archiveArtifacts allowEmptyArchive: true,
            	artifacts: coverageReportFile
            //step([$class: 'WsCleanup', notFailBuild: true])
        }
    }
}
 
return this;
