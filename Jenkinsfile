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

        stage('Scan') {
            steps {
                script {
                    // Zmiana uprawnień do pliku mvnw
                    sh 'chmod +x ./mvnw'
                    if (!fileExists('./mvnw')) {
                        error 'Plik mvnw nie został znaleziony w katalogu projektu. Upewnij się, że Maven Wrapper został dodany.'
                    }
                }
                withSonarQubeEnv(installationName: 'SQ1') {
                    // Uruchomienie analizy SonarQube, która korzysta z konfiguracji zawartej w sonar-project.properties
                    sh './mvnw clean org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.0.2155:sonar'
                }
            }
        }
   
        stage('Build') {
            steps {
                echo 'Buduję wersję developerską aplikacji...'
                // Przykładowe polecenie do budowania aplikacji (np. Maven)
                sh './mvnw clean install' // Jeśli korzystasz z Maven Wrapper
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
