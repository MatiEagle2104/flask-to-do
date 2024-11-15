pipeline {
    agent any

    stages {
        stage('Clone Repository') {
            steps {
                git branch: 'main', url: 'https://github.com/MatiEagle2104/flask-todo-app.git'
            }
        }
        stage('Install Dependencies') {
            steps {
                sh 'pip install -r requirements.txt'
            }
        }
        stage('Run Tests') {
            steps {
                sh 'pytest tests/'
            }
        }
        stage('Deploy Application') {
            steps {
                // Uruchom aplikację w tle
                sh 'nohup python3 app.py &'
            }
        }
    }
}
