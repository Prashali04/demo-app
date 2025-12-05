pipeline {
    agent any
    
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
        
        stage('Package') {
            steps {
                sh 'mvn package -DskipTests'
                archiveArtifacts 'target/*.war'
            }
        }
        
        stage('Deploy to Tomcat') {
            steps {
                script {
                    echo "Undeploying existing application..."
                    sh '''
                        curl -s -u jenkins:jenkins123 http://localhost:8081/manager/text/undeploy?path=/demo-app || true
                        sleep 3
                    '''
                    echo "Deploying to Tomcat..."
                    sh '''
                        curl -s -u jenkins:jenkins123 --upload-file target/demo-app.war http://localhost:8081/manager/text/deploy?path=/demo-app
                        sleep 5
                    '''
                }
            }
        }
        
        stage('Verify') {
            steps {
                script {
                    echo "Verifying deployment..."
                    sh '''
                        # Check if application is responding
                        if curl -s http://localhost:8081/demo-app/ > /dev/null; then
                            echo "✅ Application is accessible"
                        else
                            echo "❌ Application not accessible"
                            exit 1
                        fi
                    '''
                }
            }
        }
    }
    
    post {
        always {
            echo 'Day 4 Tasks: Pipeline completed'
        }
    }
}
