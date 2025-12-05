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
                archiveArtifacts artifacts: 'target/*.war', fingerprint: true
            }
        }
        
        stage('Deploy to Tomcat') {
            steps {
                script {
                    echo 'Deploying to Tomcat...'
                    sh '''
                    curl -s -u jenkins:jenkins123 \
                      --upload-file target/demo-app.war \
                      "http://localhost:8081/manager/text/deploy?path=/demo-app"
                    sleep 5
                    '''
                }
            }
        }
        
        stage('Verify') {
            steps {
                script {
                    echo 'Verifying deployment...'
                    sh '''
                    if curl -s http://localhost:8081/demo-app/ | grep -q "Working"; then
                        echo "✅ Deployment successful!"
                    else
                        echo "❌ Deployment failed"
                        exit 1
                    fi
                    '''
                }
            }
        }
    }
    
    post {
        always {
            echo 'Day 4 Tasks: Deployment complete'
        }
    }
}
