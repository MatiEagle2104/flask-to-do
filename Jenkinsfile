pipeline {
    agent any
    options {
        buildDiscarder(logRotator(numToKeepStr: '5'))
    }

    environment {
        JAVA_HOME = '/usr/lib/jvm/java-17-openjdk-amd64'  // Zmień na odpowiednią ścieżkę do JDK 17
        PATH = "${JAVA_HOME}/bin:${env.PATH}"
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
                    // Sprawdzenie wersji Javy
                    sh 'java --version'
                    
                    // Uruchomienie skanowania SonarQube
                    sh './mvnw clean org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.0.2155:sonar'
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
