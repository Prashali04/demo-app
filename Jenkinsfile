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
        
        stage('Deploy') {
            steps {
                script {
                    echo "🚀 Starting deployment..."
                    
                    // Backup current version
                    sh '''
                        curl -s http://localhost:8081/demo-app/ >/dev/null 2>&1 && \
                        echo "Creating backup for rollback..." && \
                        curl -s -o /tmp/backup.war http://localhost:8081/demo-app/WEB-INF/classes?format=war || echo "No existing app to backup"
                    '''
                    
                    // Deploy
                    sh '''
                        echo "Undeploying old version..."
                        curl -s -u jenkins:jenkins123 "http://localhost:8081/manager/text/undeploy?path=/demo-app" || true
                        sleep 3
                        
                        echo "Deploying new version..."
                        curl -s -u jenkins:jenkins123 --upload-file target/demo-app.war \
                            "http://localhost:8081/manager/text/deploy?path=/demo-app"
                        sleep 5
                    '''
                    
                    // Verify
                    sh '''
                        for i in {1..10}; do
                            if curl -s http://localhost:8081/demo-app/ >/dev/null; then
                                echo "✅ Deployment successful!"
                                exit 0
                            fi
                            echo "Waiting... ($i/10)"
                            sleep 5
                        done
                        echo "❌ Deployment failed"
                        exit 1
                    '''
                }
            }
        }
    }
    
    post {
        always {
            echo "Build #${BUILD_NUMBER} finished with status: ${currentBuild.currentResult}"
            cleanWs()
        }
        
        success {
            echo "✅ Sending success email..."
            mail(
                to: 'youremail@example.com',  // CHANGE THIS
                subject: "SUCCESS: ${JOB_NAME} #${BUILD_NUMBER}",
                body: "Build succeeded!\n\nJob: ${JOB_NAME}\nBuild: #${BUILD_NUMBER}\nURL: ${BUILD_URL}"
            )
        }
        
        failure {
            echo "❌ Sending failure email..."
            mail(
                to: 'youremail@example.com',  // CHANGE THIS
                subject: "FAILURE: ${JOB_NAME} #${BUILD_NUMBER}",
                body: "Build failed!\n\nJob: ${JOB_NAME}\nBuild: #${BUILD_NUMBER}\nLogs: ${BUILD_URL}console\n\nCheck immediately!"
            )
            
            // Attempt rollback
            script {
                if (fileExists('/tmp/backup.war')) {
                    echo "🔄 Attempting rollback..."
                    sh '''
                        curl -s -u jenkins:jenkins123 "http://localhost:8081/manager/text/undeploy?path=/demo-app" || true
                        sleep 3
                        curl -s -u jenkins:jenkins123 --upload-file /tmp/backup.war \
                            "http://localhost:8081/manager/text/deploy?path=/demo-app"
                        echo "Rollback attempted"
                    '''
                }
            }
        }
    }
}
