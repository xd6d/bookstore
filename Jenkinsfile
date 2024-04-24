pipeline {
    agent any
    environment {
        SONAR_TOKEN = credentials('sonar-token')
    }
    stages {
        stage('Build Client') {
            steps {
                sh './gradlew -p client-rest clean build -s'
            }
        }
        stage('Build Server') {
            steps {
                sh './gradlew -p server clean build -s'
            }
        }
        stage('Scan Server Code') {
            steps {
                sh './gradlew -p server clean sonar --system-prop sonar.token=$SONAR_TOKEN'
            }
        }
    }
}