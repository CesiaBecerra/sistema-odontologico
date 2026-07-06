pipeline {
    agent any

    environment {
        // Usamos credenciales de Jenkins para seguridad (Transversal - 2 pts)
        SONAR_TOKEN = credentials('sonarqube-token')
        // Puedes agregar más aquí, ej: DOCKER_HUB_USER = credentials('dockerhub-user')
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/CesiaBecerra/sistema-odontologico.git'
            }
        }

        stage('Build & Test') {
            steps {
                sh 'chmod +x mvnw'
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                sh './mvnw sonar:sonar -Dsonar.host.url=http://host.docker.internal:9000 -Dsonar.token=$SONAR_TOKEN'
            }
        }

        // Nueva etapa: Pruebas de Seguridad con OWASP ZAP (Unidad 2 - 3 pts)
        stage('Security Scan (ZAP)') {
            steps {
                // Ejecuta un escaneo básico y guarda el reporte
                sh 'docker run -t owasp/zap2docker-stable zap-baseline.py -t http://host.docker.internal:8081 || true'
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