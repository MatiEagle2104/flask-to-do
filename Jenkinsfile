pipeline {
    agent any
    options {
        buildDiscarder(logRotator(numToKeepStr: '5'))
    }

    stages {
        stage('Install Dependencies') {
            steps {
                echo 'Instaluję zależności...'
                sh 'chmod +x ./mvnw'
            }
        }

        stage('SonarQube Scan') {
            steps {
                withSonarQubeEnv(installationName: 'SQ1') {
                    sh './mvnw clean org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.0.2155:sonar'
                }
            }
        }

        stage('Check Quality Gate') {
            steps {
                echo 'Sprawdzanie SonarQube Quality Gate...'
                script {
                    timeout(time: 1, unit: 'MINUTES') { // Ograniczenie czasu oczekiwania
                        def qg = waitForQualityGate()
                        if (qg.status != 'OK') {
                            error "SonarQube Quality Gate nie spełniony: ${qg.status}"
                        }
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
