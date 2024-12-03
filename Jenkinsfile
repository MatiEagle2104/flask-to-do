pipeline {
    agent {
        docker { image 'node:16' }  // Użyj obrazu Docker z Node.js
    }
    options {
        buildDiscarder(logRotator(numToKeepStr: '5'))
    }

    environment {
        SONARQUBE = 'SQ1'  // Nazwa zdefiniowanego serwera SonarQube w Jenkinsie
    }

    stages {
        stage('Install Dependencies') {
            steps {
                echo 'Instaluję zależności...'
                sh 'npm install'
            }
        }

        stage('Run SonarQube Scan') {
            steps {
                script {
                    // Przeprowadź skanowanie SonarQube
                    withSonarQubeEnv(SONARQUBE) {
                        sh 'npm run sonar'
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
