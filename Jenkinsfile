pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                // Si tu rama en GitHub se llama 'main', déjalo así.
                // Si se llama 'master', cámbialo en la siguiente línea.
                git branch: 'main', url: 'https://github.com/CesiaBecerra/sistema-odontologico.git'
            }
        }
        stage('Build & Test') {
            steps {
                sh './mvnw clean test'
            }
        }
        stage('SonarQube Analysis') {
            steps {
                sh './mvnw sonar:sonar -Dsonar.host.url=http://host.docker.internal:9000'
            }
        }
    }
}