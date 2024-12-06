pipeline {
  agent any
  options {
    buildDiscarder(logRotator(numToKeepStr: '5'))
  }
  stages {
    stage('Build') {
      steps {
        sh 'chmod +x ./mvnw'
        sh 'docker build -t darinpope/java-web-app:latest .'
      }
    }
    stage('Scan') {
      environment {
        TRIVY_CACHE_DIR = '~/.cache/trivy'
      }
      steps {
        sh 'trivy image --skip-db-update --skip-java-db-update --scanners vuln darinpope/java-web-app:latest'
      }
    }
  }
}
