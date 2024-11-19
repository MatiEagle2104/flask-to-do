pipeline {
    agent any

    stages {
        stage('Checkout Code') {
            steps {
                echo 'Pobieram kod z gałęzi feature...'
                checkout scm
            }
        }

        stage('Build Experimental Features') {
            steps {
                echo 'Buduję eksperymentalne funkcje...'
                sh 'echo "Budowa funkcji eksperymentalnych - gałąź feature"'
            }
        }

        stage('Run Feature Tests') {
            steps {
                echo 'Uruchamiam testy eksperymentalnych funkcji...'
                sh 'echo "Testy funkcji eksperymentalnych - gałąź feature"'
            }
        }
    }

    post {
        success {
            echo 'Pipeline zakończony sukcesem dla gałęzi feature!'
        }
        failure {
            echo 'Pipeline zakończony niepowodzeniem dla gałęzi feature.'
        }
    }
}
