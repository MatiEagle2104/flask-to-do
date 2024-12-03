pipeline {
    agent any
    options {
        buildDiscarder(logRotator(numToKeepStr: '5'))
    }

    stages {
        stage('Install Dependencies') {
            steps {
                echo 'Instaluję zależności...'
                sh 'npm install'
            }
        }

        stage('Run Development Tests') {
            steps {
                echo 'Uruchamiam testy developerskie...'
            }
        }
    }

    post {
        success {
            echo 'Pipeline zakończony sukcesem dla gałęzi dev!'
        }
        failure {
            echo 'Pipeline zakończony niepowodzeniem dla gałęzi dev.'
        }
    }
}
