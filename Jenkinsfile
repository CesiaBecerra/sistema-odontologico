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
                sh './mvnw clean verify -DforkCount=0 -Dmaven.test.failure.ignore=true'
            }
        }
        stage('SonarQube Analysis') {
            steps {
                withCredentials([string(credentialsId: 'sonarqube-token', variable: 'SONAR_TOKEN')]) {
                    sh 'chmod +x mvnw'
                    sh './mvnw sonar:sonar -Dsonar.host.url=http://host.docker.internal:9000 -Dsonar.token=$SONAR_TOKEN -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml'
                }
            }
        }
        stage('Docker Build & Deploy') {
            steps {
                script {
                    sh 'docker build -t sistema-odontologico:latest .'
                    sh 'docker stop odontologia-app || true'
                    sh 'docker rm odontologia-app || true'
                   sh 'docker run -d --name odontologia-app -p 8081:8080 sistema-odontologico:latest'
                }
            }
        }
    }
}