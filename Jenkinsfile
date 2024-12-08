pipeline {
    agent any

    parameters {
        // Stały typ skanowania: Baseline
        string(
            defaultValue: 'http://192.168.1.18:3000',
            description: 'Target URL to scan',
            name: 'TARGET'
        )
        booleanParam(
            defaultValue: true,
            description: 'Parameter to know if you want to generate a report.',
            name: 'GENERATE_REPORT'
        )
    }

    environment {
        ZAP_IMAGE = 'owasp/zap2docker-stable:latest'
        CONTAINER_NAME = 'owasp_zap_container'
        ZAP_WORK_DIR = '/zap/wrk'
        SCAN_TYPE = 'Baseline'  // Zawsze Baseline
    }

    stages {
        stage('Pull Docker Image and Start Container') {
            steps {
                script {
                    echo 'Pulling the latest OWASP ZAP Docker image...'
                    sh 'docker pull $ZAP_IMAGE'
                    echo 'Starting OWASP ZAP container...'
                    sh """
                    docker run -dt --name $CONTAINER_NAME $ZAP_IMAGE /bin/bash
                    docker exec $CONTAINER_NAME mkdir -p $ZAP_WORK_DIR
                    """
                }
            }
        }

        stage('Scanning target on OWASP ZAP container') {
            steps {
                script {
                    target = "${params.TARGET}"
                    echo "----> Scan type: $SCAN_TYPE"
                    echo "----> Target URL: $target"

                    // Zawsze wykonujemy skanowanie typu Baseline
                    sh """
                        docker exec $CONTAINER_NAME \
                        zap-baseline.py \
                        -t $target \
                        -r $ZAP_WORK_DIR/report.html \
                        -I
                    """
                }
            }
        }

        stage('Copy Report to Workspace') {
            steps {
                script {
                    echo 'Copying the report to Jenkins workspace...'
                    sh """
                    docker cp $CONTAINER_NAME:$ZAP_WORK_DIR/report.html ${WORKSPACE}/report.html
                    """
                }
            }
        }

        stage('Generate Report') {
            when {
                expression { params.GENERATE_REPORT }
            }
            steps {
                script {
                    echo 'Generating the report...'
                    // Jeżeli parametr GENERATE_REPORT jest true, generujemy raport
                    // Inne opcje mogą obejmować konwersję na HTML, PDF lub wysyłanie e-maili.
                    echo "The report is available at ${WORKSPACE}/report.html"
                }
            }
        }
    }

    post {
        always {
            echo 'Cleaning up the container...'
            sh """
                docker stop $CONTAINER_NAME
                docker rm $CONTAINER_NAME
            """
            cleanWs()  // Czyszczenie przestrzeni roboczej po zakończeniu
        }
    }
}
