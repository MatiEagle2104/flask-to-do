pipeline {
    agent any

    environment {
        ZAP_HOST = '127.0.0.1'       // Adres lokalny, na którym ZAP nasłuchuje
        ZAP_PORT = '8090'             // Port, na którym ZAP nasłuchuje
        TARGET_APP_URL = 'http://192.168.1.18:3000' // URL aplikacji docelowej
        ZAP_API_KEY = 'dqj6d907sv428fuqjl7r779s5f' // Twój klucz API ZAP
    }

    stages {
        stage('Run OWASP ZAP Spider') {
            steps {
                script {
                    echo 'Running OWASP ZAP Spider to load the application...'

                    // Uruchomienie "spidera" w celu załadowania aplikacji i dodania URL do drzewa skanowania
                    def spiderCommand = """
                    curl "http://${env.ZAP_HOST}:${env.ZAP_PORT}/JSON/spider/action/scan/?apikey=${env.ZAP_API_KEY}&url=${env.TARGET_APP_URL}&maxDepth=5&subtreeOnly=true"
                    """
                    sh spiderCommand

                    // Czekanie, aż "spider" zakończy skanowanie
                    echo 'Waiting for the spider scan to finish...'
                    sleep(30)  // Czas oczekiwania na zakończenie skanowania
                }
            }
        }

        stage('Include URL in Context') {
            steps {
                script {
                    echo 'Including URL in ZAP context...'

                    // Dodanie URL do kontekstu
                    def includeInContextCommand = """
                    curl "http://${env.ZAP_HOST}:${env.ZAP_PORT}/JSON/context/action/includeInContext/?apikey=${env.ZAP_API_KEY}&contextName=Default+Context&regex=${env.TARGET_APP_URL}.*"
                    """
                    sh includeInContextCommand
                }
            }
        }

        stage('Run OWASP ZAP Active Scan') {
            steps {
                script {
                    echo 'Starting OWASP ZAP active scan...'

                    // Uruchomienie aktywnego skanowania ZAP
                    def startScanCommand = """
                    curl "http://${env.ZAP_HOST}:${env.ZAP_PORT}/JSON/ascan/action/scan/?apikey=${env.ZAP_API_KEY}&url=${env.TARGET_APP_URL}&contextName=Default+Context&regex=${env.TARGET_APP_URL}.*"
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
