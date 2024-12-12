pipeline {
    agent any

    environment {
        ZAP_HOST = '127.0.0.1'       
        ZAP_PORT = '8090'            
        TARGET_APP_URL = 'http://192.168.1.18:3000' 
        ZAP_API_KEY = 'dqj6d907sv428fuqjl7r779s5f' 
    }

    stages {
        stage('Run OWASP ZAP Spider') {
            steps {
                script {
                    echo 'Running OWASP ZAP Spider to load the application...'

                    def spiderCommand = """
                    curl "http://${env.ZAP_HOST}:${env.ZAP_PORT}/JSON/spider/action/scan/?apikey=${env.ZAP_API_KEY}&url=${env.TARGET_APP_URL}&maxDepth=5&subtreeOnly=true"
                    """
                    sh spiderCommand

                    echo 'Waiting for the spider scan to finish...'
                    sleep(30)  
                }
            }
        }

        stage('Include URL in Context') {
            steps {
                script {
                    echo 'Including URL in ZAP context...'

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

                    def startScanCommand = """
                    curl "http://${env.ZAP_HOST}:${env.ZAP_PORT}/JSON/ascan/action/scan/?apikey=${env.ZAP_API_KEY}&url=${env.TARGET_APP_URL}&contextName=Default+Context&regex=${env.TARGET_APP_URL}.*"
                    """
                    sh startScanCommand

                    echo 'Waiting for active scan to finish...'

                    def scanCompleted = false
                    while (!scanCompleted) {
                        def scanStatusCommand = """
                        curl "http://${env.ZAP_HOST}:${env.ZAP_PORT}/JSON/ascan/view/status/?apikey=${env.ZAP_API_KEY}"
                        """
                        def scanStatusResponse = sh(script: scanStatusCommand, returnStdout: true).trim()

                        def scanStatusJson = readJSON(text: scanStatusResponse)
                        def status = scanStatusJson.status

                        echo "Scan Status: ${status}"

                        if (status == "100") {
                            scanCompleted = true
                        } else {
                            echo 'Scan not finished yet. Waiting...'
                            sleep(5)
                        }
                    }

                    echo 'Active scan completed.'
                }
            }
        }

        stage('Generate Report') {
            steps {
                script {
                    echo 'Generating OWASP ZAP report...'
                    def getReportCommand = """
                    curl -X GET "http://${env.ZAP_HOST}:${env.ZAP_PORT}/OTHER/core/other/htmlreport/?apikey=${env.ZAP_API_KEY}" --output owasp-zap-report.html
                    """
                    sh getReportCommand
                }
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
