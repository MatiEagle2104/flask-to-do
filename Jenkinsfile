pipeline {
    agent any

    environment {
        NVD_API_KEY = credentials('d7f6b61c-a33d-4fa0-9520-38c28b4d8a6d')
        SONARQUBE_SERVER = 'SonarQube' // Nazwa serwera SonarQube z konfiguracji w Jenkinsie
    }

    tools {
        nodejs 'NodeJS'  // Wybierz nazwę konfiguracji NodeJS
    }

    stages {
        stage('Checkout Code') {
            steps {
                echo 'Pobieram kod z gałęzi main...'
                checkout scm
            }
        }

        stage('Install Dependencies') {
            steps {
                echo 'Instaluje zależności npm...'
                sh 'npm install'
            }
        }

        stage('OWASP Dependency-Check Vulnerabilities') {
            steps {
                echo 'Rozpoczynam skanowanie zależności za pomocą OWASP Dependency Check...'
                script {
                    try {
                        dependencyCheck additionalArguments: ''' 
                            -o './'
                            -s './'
                            -f 'ALL' 
                            --nvdApiKey ${env.NVD_API_KEY}
                            --prettyPrint''', odcInstallation: 'owasp-dc'
                    } catch (Exception e) {
                        echo 'Nie udało się zaktualizować danych NVD. Kontynuuję skanowanie przy użyciu lokalnych danych.'
                        sh '''
                            dependency-check.sh \
                            --project "My Project" \
                            --format ALL \
                            --scan ./ \
                            --out ./ \
                            --prettyPrint
                        '''
                    }
                }
                dependencyCheckPublisher pattern: 'dependency-check-report.xml'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                echo 'Rozpoczynam skanowanie SonarQube...'
                withSonarQubeEnv('SonarQube') { // Użycie konfiguracji serwera SonarQube
                    sh '''
                        sonar-scanner \
                            -Dsonar.projectKey=your-project-key \
                            -Dsonar.sources=. \
                            -Dsonar.host.url=$SONAR_HOST_URL \
                            -Dsonar.login=$SONAR_AUTH_TOKEN
                    '''
                }
            }
        }

        stage('Wait for SonarQube Quality Gate') {
            steps {
                echo 'Czekam na wynik Quality Gate SonarQube...'
                waitForQualityGate abortPipeline: true
            }
        }

        stage('Build') {
            steps {
                echo 'Buduję stabilną wersję aplikacji...'
                sh 'echo "Brak kodu do budowy - gałąź main"'
            }
        }

        stage('Deploy to Production') {
            steps {
                echo 'Wdrażam aplikację na środowisko produkcyjne...'
                sh 'echo "Brak wdrożenia - gałąź main"'
            }
        }
    }

    post {
        success {
            echo 'Pipeline zakończony sukcesem dla gałęzi main!'
        }
        failure {
            echo 'Pipeline zakończony niepowodzeniem dla gałęzi main.'
        }
    }
}
