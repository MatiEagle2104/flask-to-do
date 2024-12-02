pipeline {
    agent {
        docker {
            image 'node:16'  // Wybierz odpowiednią wersję Node.js (np. node:16)
            label 'my-docker-agent'  // Opcjonalnie, jeśli masz etykiety dla swoich agentów
        }
    }

    environment {
        NVD_API_KEY = credentials('d7f6b61c-a33d-4fa0-9520-38c28b4d8a6d')
    }

    stages {
        stage('Checkout Code') {
            steps {
                echo 'Pobieram kod z gałęzi main...'
                checkout scm
            }
        }

        stage('Install Dependencies') {
            steps {
                echo 'Instaluje zależności npm...'
                sh 'npm install'
            }
        }

        stage('OWASP Dependency-Check Vulnerabilities') {
            steps {
                echo 'Rozpoczynam skanowanie zależności za pomocą OWASP Dependency Check...'
                dependencyCheck additionalArguments: ''' 
                    -o './'
                    -s './'
                    -f 'ALL' 
                    --nvdApiKey ${env.NVD_API_KEY}
                    --prettyPrint''', odcInstallation: 'owasp-dc'

                // Publikowanie raportu
                dependencyCheckPublisher pattern: 'dependency-check-report.xml'

                // Wyświetlenie zawartości raportu w logach Jenkinsa
                script {
                    echo 'Wyniki skanowania zależności:'
                    sh 'cat dependency-check-report.xml'
                }
            }
        }

        stage('Build') {
            steps {
                echo 'Buduję stabilną wersję aplikacji...'
                sh 'echo "Brak kodu do budowy - gałąź main"'
            }
        }

        stage('Deploy to Production') {
            steps {
                echo 'Wdrażam aplikację na środowisko produkcyjne...'
                sh 'echo "Brak wdrożenia - gałąź main"'
            }
        }
    }

    post {
        success {
            echo 'Pipeline zakończony sukcesem dla gałęzi main!'
        }
        failure {
            echo 'Pipeline zakończony niepowodzeniem dla gałęzi main.'
        }
    }
}
