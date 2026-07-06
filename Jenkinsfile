pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/CesiaBecerra/sistema-odontologico.git'
            }
        }
        stage('Compilar y Test') {
            steps {
                sh 'mvn clean test'
            }
        }
        stage('SonarQube Analysis') {
            steps {
                // Aquí Jenkins enviará el reporte a tu contenedor de SonarQube
                sh 'mvn sonar:sonar -Dsonar.host.url=http://host.docker.internal:9000'
            }
        }
    }
}