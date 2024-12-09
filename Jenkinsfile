pipeline {
    agent any

    environment {
        ZAP_HOST = '127.0.0.1'       // Adres lokalny, na którym ZAP nasłuchuje
        ZAP_PORT = '8090'             // Port, na którym ZAP nasłuchuje
        TARGET_APP_URL = 'http://192.168.1.18:3000' // URL aplikacji docelowej
        ZAP_API_KEY = 'sd8fr5tbjfp8t6hrpf832s68l7' // Twój klucz API ZAP
    }

    stages {
        stage('Run OWASP ZAP Scan') {
            steps {
                script {
                    echo 'Starting OWASP ZAP scan...'

                    // Uruchomienie skanowania aplikacji w ZAP
                    def zapScanCommand = """
                    curl "http://${env.ZAP_HOST}:${env.ZAP_PORT}/JSON/context/action/includeInContext/?apikey=${env.ZAP_API_KEY}&contextName=Default+Context&regex=${env.TARGET_APP_URL}.*"
                    """
                    sh zapScanCommand

                    // Uruchomienie aktywnego skanowania ZAP
                    def startScanCommand = """
                    curl "http://${env.ZAP_HOST}:${env.ZAP_PORT}/JSON/ascan/action/scan/?apikey=${env.ZAP_API_KEY}&url=${env.TARGET_APP_URL}"
                    """
                    sh startScanCommand
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
