pipeline {
    agent any

    environment {
        ZAP_HOST = '192.168.1.18'
        ZAP_PORT = '8090'
        TARGET_APP_URL = 'http://192.168.1.18:3000'
    }

    stages {
        stage('Run OWASP ZAP Scan') {
            steps {
                script {
                    echo 'Starting OWASP ZAP scan...'
                    // Uruchomienie skanowania OWASP ZAP
                    def zapScanCommand = """
                    curl -X POST http://${env.ZAP_HOST}:${env.ZAP_PORT}/JSON/ascan/action/scan/ \
                        --data "url=${env.TARGET_APP_URL}&recurse=true"
                    """
                    sh zapScanCommand

                    // Sprawdzenie statusu skanowania (polling)
                    timeout(time: 5, unit: 'MINUTES') {
                        waitUntil {
                            def statusCheckCommand = """
                            curl -s "http://${env.ZAP_HOST}:${env.ZAP_PORT}/JSON/ascan/view/status/" | jq '.status'
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
                    curl -X GET http://${env.ZAP_HOST}:${env.ZAP_PORT}/OTHER/core/other/htmlreport/ \
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
        success {
            echo 'OWASP ZAP scan completed successfully.'
        }
        failure {
            echo 'OWASP ZAP scan failed. Check the logs and report for more details.'
        }
    }
}
