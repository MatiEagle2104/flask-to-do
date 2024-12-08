pipeline {
    agent any

    environment {
        ZAP_HOST = '127.0.0.1'   // Adres lokalny, na którym ZAP nasłuchuje
        ZAP_PORT = '9091'         // Port, na którym ZAP nasłuchuje
        TARGET_APP_URL = 'http://192.168.1.18:3000'
        ZAP_LOG_PATH = '/path/to/zap.log'  // Ścieżka do logów ZAP
    }

    stages {
        stage('Setup') {
            steps {
                script {
                    echo 'Starting ZAP...'
                    // Uruchomienie ZAP w tle
                    sh """
                    /usr/share/zaproxy/zap.sh -daemon -port ${env.ZAP_PORT} > ${env.ZAP_LOG_PATH} 2>&1 &
                    """
                }
            }
        }

        stage('Wait for ZAP Ready') {
            steps {
                script {
                    echo 'Waiting for ZAP to be ready...'

                    // Monitorowanie logów ZAP, szukanie komunikatu "ZAP is now listening"
                    def logCheckCommand = """
                    tail -f ${env.ZAP_LOG_PATH} | while read line; do
                        if echo "\$line" | grep -q 'ZAP is now listening on localhost:${env.ZAP_PORT}'; then
                            echo 'ZAP is ready!'
                            exit 0
                        fi
                    done
                    """
                    // Uruchomienie monitorowania logów ZAP
                    sh script: logCheckCommand, returnStatus: true
                }
            }
        }

        stage('Run OWASP ZAP Scan') {
            steps {
                script {
                    echo 'Starting OWASP ZAP scan...'

                    // Uruchomienie skanowania OWASP ZAP
                    def zapScanCommand = """
                    curl -X POST "http://${env.ZAP_HOST}:${env.ZAP_PORT}/JSON/ascan/action/scan/?url=${env.TARGET_APP_URL}&recurse=true"
                    """
                    sh zapScanCommand

                    // Sprawdzenie statusu skanowania (polling)
                    timeout(time: 10, unit: 'MINUTES') {
                        waitUntil {
                            def statusCheckCommand = """
                            curl -s "http://${env.ZAP_HOST}:${env.ZAP_PORT}/JSON/ascan/view/status/" | jq '.status'
                            """
                            def scanStatus = sh(script: statusCheckCommand, returnStdout: true).trim()
                            echo "Scan status: ${scanStatus}%"
                            return scanStatus == '100' // ZAP zakończyło skanowanie
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
                    curl -X GET "http://${env.ZAP_HOST}:${env.ZAP_PORT}/OTHER/core/other/htmlreport/" --output owasp-zap-report.html
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
