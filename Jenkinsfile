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
          // Uruchomienie skanowania Trivy i zapis wyników w formacie JSON
          sh 'trivy image --scanners vuln --format json --output trivy-report.json darinpope/java-web-app:latest'
        }
      }
    }
  }
}
