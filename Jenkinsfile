pipeline {
    agent {
        docker {
            image 'node:16-alpine'
            label 'my-docker-agent' 
        }
    }
    stages {
        stage('Test') {
            steps {
                script {
                    sh 'node --version'
                }
            }
        }
    }
}
