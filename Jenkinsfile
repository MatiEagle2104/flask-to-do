pipeline {
    agent any

    environment {
        BRANCH_NAME = "${env.BRANCH_NAME}" // Automatyczne pobranie nazwy gałęzi
    }

    stages {
        stage('Checkout Code') {
            steps {
                echo "Pobieram kod z gałęzi: ${BRANCH_NAME}..."
                checkout scm
            }
        }

        stage('Branch Specific Tasks') {
            steps {
                script {
                    // Wykonuj różne zadania w zależności od gałęzi
                    if (BRANCH_NAME == 'main') {
                        echo 'To jest gałąź główna. Możesz tu dodać zadania produkcyjne.'
                    } else if (BRANCH_NAME == 'dev') {
                        echo 'To jest gałąź developerska. Dodaj zadania dla środowiska testowego.'
                    } else if (BRANCH_NAME == 'bug-fixing') {
                        echo 'Gałąź naprawiania błędów. Możesz dodać testy naprawy błędów.'
                    } else if (BRANCH_NAME == 'feature') {
                        echo 'Gałąź funkcjonalności. Przygotuj testy eksperymentalne.'
                    } else {
                        echo "Gałąź ${BRANCH_NAME} nie ma zdefiniowanych zadań specyficznych."
                    }
                }
            }
        }
    }

    post {
        success {
            echo "Pipeline zakończony sukcesem dla gałęzi: ${BRANCH_NAME}."
        }
        failure {
            echo "Pipeline zakończony niepowodzeniem dla gałęzi: ${BRANCH_NAME}."
        }
    }
}
