pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/CesiaBecerra/sistema-odontologico.git'
            }
        }
        stage('Build & Test') {
            steps {
                sh 'chmod +x mvnw'
                // Usamos 'verify' para asegurar que JaCoCo genere el reporte para SonarQube
                sh './mvnw clean verify -DforkCount=0 -Dmaven.test.failure.ignore=true'
            }
        }
        stage('SonarQube Analysis') {
            steps {
                withCredentials([string(credentialsId: 'sonarqube-token', variable: 'SONAR_TOKEN')]) {
                    sh 'chmod +x mvnw'
                    // Especificamos la ruta del reporte para que la cobertura pase de 0.0%
                    sh './mvnw sonar:sonar -Dsonar.host.url=http://host.docker.internal:9000 -Dsonar.token=$SONAR_TOKEN -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml'
                }
            }
        }
        stage('Docker Build & Deploy') {
            steps {
                script {
                    // Construye la imagen usando tu Dockerfile
                    sh 'docker build -t sistema-odontologico:latest .'
                    // Limpia contenedor previo y levanta el nuevo
                    sh 'docker stop odontologia-app || true'
                    sh 'docker rm odontologia-app || true'
                    sh 'docker run -d --name odontologia-app -p 8080:8080 sistema-odontologico:latest'
                }
            }
        }
    }
}