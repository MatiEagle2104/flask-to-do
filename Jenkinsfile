pipeline {
    agent any

    environment {
        ZAP_HOST = '127.0.0.1'   // Adres lokalny, na którym ZAP nasłuchuje
        ZAP_PORT = '8090'         // Port, na którym ZAP nasłuchuje
        TARGET_APP_URL = 'http://192.168.1.18:3000'  // URL aplikacji do skanowania
    }

    stages {
        stage('Setup') {
            steps {
                script {
                    echo 'Starting ZAP...'
                    // Uruchomienie ZAP w tle
                    sh """
                    /usr/share/zaproxy/zap.sh -daemon -port ${env.ZAP_PORT} &
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
                    while ! curl -s "http://${env.ZAP_HOST}:${env.ZAP_PORT}/JSON/core/view/version"; do
                        echo "Waiting for ZAP to start..."
                    done
                    echo 'ZAP is ready!'
                    """
                    // Uruchomienie monitorowania logów ZAP
                    sh script: logCheckCommand
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
