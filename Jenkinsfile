pipeline {
    agent any
    
    tools {
        maven 'Maven_3.8.4'
        jdk 'JDK_11'
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Build') {
            steps {
                sh 'mvn clean compile'
            }
        }
        
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
        
        stage('Code Coverage') {
            steps {
                sh 'mvn jacoco:report'
            }
        }
    }
    
    post {
        always {
            // JUnit test reports
            junit 'target/surefire-reports/*.xml'
            
            // Archive the coverage report
            publishHTML([
                target: [
                    allowMissing: false,
                    alwaysLinkToLastBuild: false,
                    keepAll: true,
                    reportDir: 'target/site/jacoco',
                    reportFiles: 'index.html',
                    reportName: 'Code Coverage Report'
                ]
            ])
        }
    }
}
