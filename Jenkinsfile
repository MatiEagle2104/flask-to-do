pipeline {
    agent any

//    environment {
//        NVD_API_KEY = credentials('d7f6b61c-a33d-4fa0-9520-38c28b4d8a6d')
//    }

    tools {
        nodejs 'NodeJS'
    }

    stages {
        stage('Checkout Code') {
            steps {
                echo 'Pobieram kod z gałęzi code...'
                checkout scm
            }
        }

        stage('Install Dependencies') {
            steps {
                echo 'Instaluje zależności...'
                sh 'npm install'
            }
        }

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

        stage('Build') {
            steps {
                echo 'Buduję stabilną wersję aplikacji...'
                sh 'echo "Brak kodu do budowy - gałąź test"'
            }
        }
    }

    post {
        success {
            echo 'Pipeline zakończony sukcesem dla gałęzi code!'
        }
        failure {
            echo 'Pipeline zakończony niepowodzeniem dla gałęzi code.'
        }
    }
}
