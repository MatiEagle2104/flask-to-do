pipeline {
    agent any

    environment {
        ZAP_HOST = '127.0.0.1'           // ZAP host
        ZAP_PORT = '8090'                 // Port na którym nasłuchuje ZAP
        TARGET_APP_URL = 'http://192.168.1.18:3000'  // Adres aplikacji do przetestowania
        API_KEY = 'AfRrgmEq6WwvmYI6s3bCuGIDxEF10TIa33dypgClnGk=' // Twój klucz API ZAP
    }

    stages {
        stage('Setup ZAP') {
            steps {
                script {
                    echo 'Starting OWASP ZAP...'
                    // Uruchomienie ZAP w tle, nasłuchującego na porcie 8090
                    sh """
                    /usr/share/zaproxy/zap.sh -daemon -port ${env.ZAP_PORT}
                    """
                    // Opcjonalnie: Czekaj na ZAP, aby się uruchomił (np. 30 sek)
                    sleep(30)
                }
            }
        }

        stage('Run OWASP ZAP Scan') {
            steps {
                script {
                    echo 'Starting OWASP ZAP scan...'

                    // Uruchomienie skanowania aplikacji
                    def zapScanCommand = """
                    curl -X POST "http://${env.ZAP_HOST}:${env.ZAP_PORT}/JSON/ascan/action/scan/ \
                        ?apikey=${env.API_KEY} \
                        &url=${env.TARGET_APP_URL} \
                        &recurse=true"
                    """
                    sh zapScanCommand

                    // Sprawdzenie statusu skanowania (polling)
                    timeout(time: 5, unit: 'MINUTES') {
                        waitUntil {
                            def statusCheckCommand = """
                            curl -s "http://${env.ZAP_HOST}:${env.ZAP_PORT}/JSON/ascan/view/status/?apikey=${env.API_KEY}" | jq '.status'
                            """
                            def scanStatus = sh(script: statusCheckCommand, returnStdout: true).trim()
                            echo "Scan status: ${scanStatus}%"
                            return scanStatus == '100'
                        }
                    }
                }
            }
        }

        stage('Generate Report') {
            steps {
                script {
                    echo 'Generating OWASP ZAP report...'

                    // Pobranie raportu HTML z OWASP ZAP
                    def getReportCommand = """
                    curl -X GET "http://${env.ZAP_HOST}:${env.ZAP_PORT}/OTHER/core/other/htmlreport/?apikey=${env.API_KEY}" \
                        --output owasp-zap-report.html
                    """
                    sh getReportCommand
                }
                // Zachowanie raportu jako artefaktu
                archiveArtifacts artifacts: 'owasp-zap-report.html', allowEmptyArchive: false
            }
        }
    }

    post {
        always {
            script {
                echo 'Cleaning up...'
                // Możesz dodać tutaj jakiekolwiek operacje czyszczące, jeśli są potrzebne
            }
        }
        success {
            echo 'OWASP ZAP scan completed successfully.'
        }
        failure {
            echo 'OWASP ZAP scan failed. Check the logs and report for more details.'
        }
    }
}
