pipeline {
    agent any

    stages {
        stage('Checkout Code') {
            steps {
                echo 'Pobieram pliki z repozytorium...'
                checkout scm
            }
        }
    }

    post {
        success {
            echo 'Pobieranie zakończone sukcesem!'
        }
        failure {
            echo 'Pobieranie zakończone niepowodzeniem.'
        }
    }
}
