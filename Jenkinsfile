pipeline {
    agent any

    stages {
        stage('Checkout Code') {
            steps {
                echo 'Pobieram kod z gałęzi bug-fixing...'
                checkout scm
            }
        }

        stage('Run Tests') {
            steps {
                echo 'Uruchamiam testy naprawiające błędy...'
                sh 'echo "Testy naprawcze - gałąź bug-fixing"'
            }
        }

        stage('Validate Fixes') {
            steps {
                echo 'Weryfikuję poprawki błędów...'
                sh 'echo "Weryfikacja poprawek - gałąź bug-fixing"'
            }
        }
    }

    post {
        success {
            echo 'Pipeline zakończony sukcesem dla gałęzi bug-fixing!'
        }
        failure {
            echo 'Pipeline zakończony niepowodzeniem dla gałęzi bug-fixing.'
        }
    }
}
