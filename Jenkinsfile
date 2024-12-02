pipeline {
    agent any

    environment {
        NVD_API_KEY = credentials('d7f6b61c-a33d-4fa0-9520-38c28b4d8a6d')
        SONARQUBE_SERVER = 'Jenkins-SolarQube' // Nazwa serwera SonarQube z konfiguracji w Jenkinsie
    }

    tools {
        nodejs 'NodeJS'
        git 'Default' // Zgodna konfiguracja Git
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
                            --nvdApiKey $NVD_API_KEY
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
