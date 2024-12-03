pipeline {
    agent any

    stages {
        stage('Checkout Code') {
            steps {
                echo 'Pobieram kod z gałęzi dev...'
                checkout scm
            }
        }

        stage('SonarQube Code Analysis') {
            steps {
                script {
                    def scannerHome = tool name: 'SQ1', type: 'hudson.plugins.sonar.SonarRunnerInstallation'
                    withSonarQubeEnv('SQ1') {
                        sh "${scannerHome}/bin/sonar-scanner"
                    }
                }
            }
        }

        stage("SonarQube Quality Gate Check") {
            steps {
                script {
                    def qualityGate = waitForQualityGate()
                    if (qualityGate.status != 'OK') {
                        error "Quality Gate failed: ${qualityGate.status}"
                    } else {
                        echo "SonarQube Quality Gates Passed"
                    }
                }
            }
        }

        stage('Build') {
            steps {
                echo 'Buduję wersję developerską aplikacji...'
            }
        }

        stage('Run Development Tests') {
            steps {
                echo 'Uruchamiam testy developerskie...'
            }
        }
    }

    post {
        success {
            echo 'Pipeline zakończony sukcesem dla gałęzi dev!'
        }
        failure {
            echo 'Pipeline zakończony niepowodzeniem dla gałęzi dev.'
        }
    }
}
