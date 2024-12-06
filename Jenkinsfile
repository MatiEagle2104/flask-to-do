pipeline {
  agent any
  options {
    buildDiscarder(logRotator(numToKeepStr: '5'))
  }
  stages {
    stage('Build') {
      steps {
        sh 'chmod +x /var/run/docker.sock'
        sh 'docker build -t darinpope/java-web-app:latest .'
      }
    }
    stage('Scan') {
      steps {
        sh 'trivy darinpope/java-web-app:latest'
      }
    }
  }
}
