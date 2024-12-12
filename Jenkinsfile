pipeline {
    agent any

    options {
        buildDiscarder(logRotator(numToKeepStr: '5'))
    }

    environment {
        JAVA_HOME = '/usr/lib/jvm/java-17-openjdk-amd64'
        PATH = "${JAVA_HOME}/bin:${env.PATH}"
        ZAP_HOST = '127.0.0.1'
        ZAP_PORT = '8090'
        TARGET_APP_URL = 'http://192.168.1.18:3000'
        ZAP_API_KEY = 'dqj6d907sv428fuqjl7r779s5f'
        DOCKER_IMAGE = 'darinpope/java-web-app'
        DOCKER_TAG = 'latest'
    }

    tools {
        nodejs 'NodeJS'
    }

    stages {
        stage('OWASP Dependency-Check Vulnerabilities') {
            steps {
                echo 'Rozpoczynam skanowanie zależności za pomocą OWASP Dependency Check...'
                dependencyCheck additionalArguments: ''' 
                    -o './'
                    -s './'
                    -f 'ALL' 
                    --prettyPrint''', odcInstallation: 'owasp-dc'

                dependencyCheckPublisher pattern: 'dependency-check-report.xml'
            }
        }

        stage('SonarQube Scan') {
            steps {
                withSonarQubeEnv(installationName: 'SQ1') {
                    sh 'chmod +x ./mvnw'
                    sh './mvnw clean org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.0.2155:sonar'
                }
            }
        }

        stage('Trivy Image Scan') {
            steps {
                script {
                    sh 'docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} .'
                    sh 'trivy image --scanners vuln ${DOCKER_IMAGE}:${DOCKER_TAG}'
                }
            }
        }

        stage('OWASP ZAP Spider and Active Scan') {
            steps {
                script {
                    echo 'Running OWASP ZAP Spider to load the application...'

                    def spiderCommand = """
                    curl \"http://${env.ZAP_HOST}:${env.ZAP_PORT}/JSON/spider/action/scan/?apikey=${env.ZAP_API_KEY}&url=${env.TARGET_APP_URL}&maxDepth=5&subtreeOnly=true\"
                    """
                    sh spiderCommand

                    echo 'Waiting for the spider scan to finish...'
                    sleep(30)  
                }

                script {
                    echo 'Including URL in ZAP context...'

                    def includeInContextCommand = """
                    curl \"http://${env.ZAP_HOST}:${env.ZAP_PORT}/JSON/context/action/includeInContext/?apikey=${env.ZAP_API_KEY}&contextName=Default+Context&regex=${env.TARGET_APP_URL}.*\"
                    """
                    sh includeInContextCommand
                }

                script {
                    echo 'Starting OWASP ZAP active scan...'

                    def startScanCommand = """
                    curl \"http://${env.ZAP_HOST}:${env.ZAP_PORT}/JSON/ascan/action/scan/?apikey=${env.ZAP_API_KEY}&url=${env.TARGET_APP_URL}&contextName=Default+Context&regex=${env.TARGET_APP_URL}.*\"
                    """
                    sh startScanCommand

                    echo 'Waiting for active scan to finish...'

                    def scanCompleted = false
                    while (!scanCompleted) {
                        def scanStatusCommand = """
                        curl \"http://${env.ZAP_HOST}:${env.ZAP_PORT}/JSON/ascan/view/status/?apikey=${env.ZAP_API_KEY}\"
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

        stage('Generate OWASP ZAP Report') {
            steps {
                script {
                    echo 'Generating OWASP ZAP report...'
                    def getReportCommand = """
                    curl -X GET \"http://${env.ZAP_HOST}:${env.ZAP_PORT}/OTHER/core/other/htmlreport/?apikey=${env.ZAP_API_KEY}\" --output owasp-zap-report.html
                    """
                    sh getReportCommand
                }
                archiveArtifacts artifacts: 'owasp-zap-report.html', allowEmptyArchive: false
            }
        }
    }

    post {
        success {
            echo 'Pipeline zakończony sukcesem dla gałęzi main!'
        }
        failure {
            echo 'Pipeline zakończony niepowodzeniem dla gałęzi main. Sprawdź logi i raporty.'
        }
    }
}
