pipeline {
    agent any
    options {
        buildDiscarder(logRotator(numToKeepStr: '5'))
    }

    environment {
        SONARQUBE = 'Jenkins-SonarQube'  // Nazwa zdefiniowanego serwera SonarQube w Jenkinsie
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
                sh 'npm install'
            }
        }

        stage('Run SonarQube Scan') {
            steps {
                script {
                    // Przeprowadzamy skanowanie SonarQube dla projektu Node.js
                    withSonarQubeEnv(SONARQUBE) {
                        sh 'npm run sonar'  // Zakładając, że masz skrypt "sonar" w package.json
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
