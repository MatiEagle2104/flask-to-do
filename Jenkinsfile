pipeline {
    agent { label 'linux' }
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

        stage('Scan') {
            steps {
                withSonarQubeEnv(installationName: 'SQ1') {
                    sh './mvnw clean org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.0.2155:sonar'
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
