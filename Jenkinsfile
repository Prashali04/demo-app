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
                    echo "Deploying to Tomcat..."
                    sh '''
                        curl -s -u jenkins:jenkins123 "http://localhost:8081/manager/text/undeploy?path=/demo-app" || true
                        sleep 3
                        curl -s -u jenkins:jenkins123 --upload-file target/demo-app.war \
                            "http://localhost:8081/manager/text/deploy?path=/demo-app"
                        sleep 5
                    '''
                }
            }
        }
    }
    
    post {
        always {
            echo 'Pipeline finished.'
        }
    }
}
