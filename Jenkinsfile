pipeline {
    agent any

    environment {
        NVD_API_KEY = credentials('d7f6b61c-a33d-4fa0-9520-38c28b4d8a6d')  // Użyj ID dodanego w Jenkins
    }

    tools {
        nodejs 'NodeJS'  // Wybierz nazwę konfiguracji NodeJS
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
                sh 'rm -rf node_modules package-lock.json'
                sh 'npm install'
            }
        }

        stage('OWASP Dependency-Check Vulnerabilities') {
            steps {
                echo 'Rozpoczynam skanowanie zależności za pomocą OWASP Dependency Check...'
                dependencyCheck additionalArguments: ''' 
                    -o './'
                    -s './requirements.txt'
                    -f 'ALL' 
                    --nvdApiKey="${env.NVD_API_KEY}"
                    --prettyPrint''', odcInstallation: 'owasp-dc'
                
                // Publikowanie raportu
                dependencyCheckPublisher pattern: 'dependency-check-report.xml'
            }
        }

        stage('Check for Critical or High Vulnerabilities') {
            steps {
                script {
                    echo 'Sprawdzam raport pod kątem krytycznych lub wysokich podatności...'

                    // Sprawdzanie, czy w raporcie są krytyczne lub wysokie podatności
                    def highOrCriticalVulnerabilities = sh(script: '''
                        grep -i -e "critical" -e "high" dependency-check-report.xml || true
                    ''', returnStdout: true).trim()

                    if (highOrCriticalVulnerabilities) {
                        // Jeśli wykryto wysoką lub krytyczną podatność, pokazujemy cały raport
                        echo 'Wykryto wysokie lub krytyczne podatności!'

                        // Zatrzymujemy pipeline i pokazujemy komunikat o niepowodzeniu
                        error 'Pipeline zakończony niepowodzeniem ze względu na wykrycie poważnej podatności w wykorzystywanych zależnościach.'
                    } else {
                        echo 'Brak wysokich lub krytycznych podatności.'
                    }
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
