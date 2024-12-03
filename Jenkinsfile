pipeline {
    agent any
    options {
        buildDiscarder(logRotator(numToKeepStr: '5'))
    }

    stages {
        stage('Checkout Code') {
            steps {
                echo 'Pobieram kod z gałęzi dev...'
                checkout scm
            }
        }

        stage('SonarQube analysis') {
            steps {
                withSonarQubeEnv('xxxx-movil-sonarqube') {
            // requires SonarQube Scanner for Gradle 2.1+
            // It's important to add --info because of SONARJNKNS-281
                    sh './gradlew --info sonarqube  -Dsonar.branch.name=${GIT_BRANCH}'
                }
            }
        }

        stage("Quality Gate"){
            steps {
                timeout(time: 600, unit: 'SECONDS') {
                    script{
                        def qg = waitForQualityGate()
                        if (qg.status != 'OK') {
                            error "Se aborta la pipeline debido a que no se superan los umbrales de calidad: ${qg.status}"
                        }
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
