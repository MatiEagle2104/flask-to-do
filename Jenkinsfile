pipeline {
    agent any

    environment {
        ZAP_HOST = '127.0.0.1'       // Adres lokalny, na którym ZAP nasłuchuje
        ZAP_PORT = '8090'             // Port, na którym ZAP nasłuchuje
        TARGET_APP_URL = 'http://192.168.1.18:3000' // URL aplikacji docelowej
        ZAP_API_KEY = 'sd8fr5tbjfp8t6hrpf832s68l7' // Twój klucz API ZAP
    }

    stages {
        stage('Setup') {
            steps {
                script {
                    echo 'Starting ZAP...'
                    // Uruchomienie ZAP w tle z kluczem API
                    sh """
                    /usr/share/zaproxy/zap.sh -daemon -port ${env.ZAP_PORT} -config api.key=${env.ZAP_API_KEY} &
                    """
                }
            }
        }

        stage('Wait for ZAP Ready') {
            steps {
                script {
                    echo 'Waiting for ZAP to be ready...'
                    
                    // Usunięcie monitorowania logów, jeśli logi nie istnieją
                    echo 'Assuming ZAP is ready without log monitoring.'
                    sleep(time: 10, unit: 'SECONDS') // Czekanie 10 sekund, aby dać ZAP czas na uruchomienie
                }
            }
        }

        stage('Run OWASP ZAP Scan') {
            steps {
                script {
                    echo 'Starting OWASP ZAP scan...'

                    // Uruchomienie skanowania OWASP ZAP z użyciem klucza API
                    def zapScanCommand = """
                    curl -X POST "http://${env.ZAP_HOST}:${env.ZAP_PORT}/JSON/ascan/action/scan/?url=${env.TARGET_APP_URL}&recurse=true&apikey=${env.ZAP_API_KEY}" -H "Content-Type: application/json"
                    """
                    sh zapScanCommand

                    // Sprawdzenie statusu skanowania (polling)
                    timeout(time: 10, unit: 'MINUTES') {
                        waitUntil {
                            def statusCheckCommand = """
                            curl -s "http://${env.ZAP_HOST}:${env.ZAP_PORT}/JSON/ascan/view/status/?apikey=${env.ZAP_API_KEY}" | jq '.status'
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
                    // Pobranie raportu HTML z OWASP ZAP z użyciem klucza API
                    def getReportCommand = """
                    curl -X GET "http://${env.ZAP_HOST}:${env.ZAP_PORT}/OTHER/core/other/htmlreport/?apikey=${env.ZAP_API_KEY}" --output owasp-zap-report.html
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
