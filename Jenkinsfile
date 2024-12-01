pipeline {
    agent any

    stages {
        stage('Checkout Code') {
            steps {
                echo 'Pobieram kod z gałęzi main...'
                checkout scm
            }
        }

        stage('OWASP Dependency Check') {
            steps {
                echo 'Rozpoczynam skanowanie zależności za pomocą OWASP Dependency Check...'
                dependencyCheck additionalArguments: '', 
                               odcInstallation: 'OWASP Dependency Check', 
                               configFile: '', 
                               failBuildOnCVSS: 7, 
                               healthy: 0, 
                               numberOfDependencies: 0, 
                               refThreshold: 0, 
                               skipArchive: false
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
