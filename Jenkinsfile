pipeline {
    agent any

    tools {
        nodejs 'NodeJS'
    }

    stages {
        stage('Checkout Code') {
            steps {
                echo 'Pobieram kod z gałęzi code...'
                checkout scm
            }
        }

        stage('Install Dependencies') {
            steps {
                echo 'Instaluje zależności...'
                sh 'npm install'
            }
        }

        stage('OWASP Dependency-Check Vulnerabilities') {
            steps {
                script {
                    echo 'Rozpoczynam skanowanie zależności za pomocą OWASP Dependency Check...'
                    dependencyCheck additionalArguments: ''' 
                        -o './'
                        -s './'
                        -f 'ALL' 
                        --prettyPrint''', odcInstallation: 'owasp-dc'

                    dependencyCheckPublisher pattern: 'dependency-check-report.xml'

                    // Analiza raportu dla HIGH/CRITICAL podatności
                    def reportFile = readFile('dependency-check-report.json')
                    def reportJson = readJSON text: reportFile

                    def vulnerabilities = reportJson.dependencies.findAll { dep ->
                        dep.vulnerabilities?.find { vul ->
                            vul.severity in ['HIGH', 'CRITICAL']
                        }
                    }

                    if (vulnerabilities) {
                        echo "Znaleziono ${vulnerabilities.size()} podatności HIGH/CRITICAL."
                        error 'Pipeline zakończony niepowodzeniem ze względu na podatności HIGH/CRITICAL.'
                    } else {
                        echo 'Nie znaleziono podatności HIGH/CRITICAL.'
                    }
                }
            }
        }

        stage('Build') {
            steps {
                echo 'Buduję stabilną wersję aplikacji...'
                sh 'echo "Brak kodu do budowy - gałąź test"'
            }
        }
    }

    post {
        success {
            echo 'Pipeline zakończony sukcesem dla gałęzi code!'
        }
        failure {
            echo 'Pipeline zakończony niepowodzeniem dla gałęzi code.'
        }
    }
}
