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
