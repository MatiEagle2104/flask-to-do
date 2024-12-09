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
      steps {
        script {
          // Uruchomienie skanowania Trivy na obrazie darinpope/java-web-app:latest
          sh 'trivy image --scanners vuln --output trivy-report.json darinpope/java-web-app:latest'
        }
      }
    }
  }
}

