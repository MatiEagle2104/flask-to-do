pipeline {
    agent any
    options {
        buildDiscarder(logRotator(numToKeepStr: '5'))
    }

    environment {
        SONARQUBE = 'SQ1'  // Nazwa zdefiniowanego serwera SonarQube w Jenkinsie
    }

    stages {
        stage('Checkout Code') {
            steps {
                echo 'Pobieram kod z repozytorium...'
                checkout scm
            }
        }

        stage('Install Dependencies') {
            steps {
                echo 'Instaluję zależności...'
                sh 'chmod +x ./mvn'
                //sh './npmw install'
            }
        }

        stage('Run SonarQube Scan') {
            steps {
                script {
                    // Przeprowadzamy skanowanie SonarQube dla projektu Node.js
                    withSonarQubeEnv(SONARQUBE) {
                        sh './mvn run sonar'  // Zakładając, że masz skrypt "sonar" w package.json
                    }
                }
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
