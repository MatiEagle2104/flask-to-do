pipeline {
    agent any
    options {
        buildDiscarder(logRotator(numToKeepStr: '5'))
    }

    stages {
        stage('Install Dependencies') {
            steps {
                echo 'Instaluję zależności...'
            }
        }

        stage('SonarQube Scan') {
            steps {
                withSonarQubeEnv(installationName: 'SQ1') {
                    sh './mvnw clear org.sonarsource.scanner.maven:sonar-maver-plugin:3.9.0.2155:sonar'
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
