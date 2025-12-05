pipeline {
    agent any
    
    environment {
        TOMCAT_URL = 'http://localhost:8081'
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
        
        stage('Package') {
            steps {
                sh 'mvn package -DskipTests'
                archiveArtifacts 'target/*.war'
            }
        }
        
        stage('Deploy to Staging') {
            steps {
                script {
                    echo "🚀 Starting deployment..."
                    
                    try {
                        // Backup current version
                        sh '''
                            echo "Creating backup for rollback..."
                            curl -s http://localhost:8081/demo-app/ >/dev/null 2>&1 && \
                            curl -s -o /tmp/backup.war http://localhost:8081/demo-app/WEB-INF/classes?format=war || true
                        '''
                        
                        // Deploy new version
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
                        echo "Verifying deployment..."
                        sh '''
                            for i in {1..10}; do
                                if curl -s http://localhost:8081/demo-app/ >/dev/null; then
                                    echo "✅ Deployment successful!"
                                    exit 0
                                fi
                                sleep 5
                            done
                            echo "❌ Deployment failed"
                            exit 1
                        '''
                        
                    } catch (Exception e) {
                        echo "❌ Deployment failed, attempting rollback..."
                        
                        // Rollback
                        sh '''
                            if [ -f /tmp/backup.war ]; then
                                echo "Rolling back..."
                                curl -s -u jenkins:jenkins123 "http://localhost:8081/manager/text/undeploy?path=/demo-app" || true
                                sleep 3
                                curl -s -u jenkins:jenkins123 --upload-file /tmp/backup.war \
                                    "http://localhost:8081/manager/text/deploy?path=/demo-app"
                                echo "✅ Rollback completed"
                            fi
                        '''
                        
                        error "Deployment failed"
                    }
                }
            }
        }
    }
    
    post {
        always {
            echo "Build #${BUILD_NUMBER} - ${currentBuild.result}"
        }
    }
}
