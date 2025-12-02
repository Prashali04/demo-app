pipeline {
    agent any
    
    tools {
        maven 'Maven_3.8.4'
        jdk 'JDK_11'
    }
    
    environment {
        SONAR_TOKEN = credentials('sonarqube-token')
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
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                    echo '✅ Task 1: Automated Testing Complete - JUnit reports generated'
                }
            }
        }
        
        stage('Code Coverage') {
            steps {
                sh 'mvn jacoco:report'
            }
            post {
                always {
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
                    echo '✅ Task 2: Code Coverage Complete - JaCoCo reports available'
                }
            }
        }
        
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh '''
                    mvn sonar:sonar \
                      -Dsonar.projectKey=demo-app-java \
                      -Dsonar.projectName="Demo Java App" \
                      -Dsonar.java.binaries=target/classes \
                      -Dsonar.token=${SONAR_TOKEN}
                    '''
                }
            }
            post {
                success {
                    echo '✅ Additional Challenge: SonarQube Analysis Complete'
                }
            }
        }
        
        stage('Quality Gate Check') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
        
        stage('Package') {
            steps {
                sh 'mvn package -DskipTests'
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }
    }
    
    post {
        always {
            echo '=== DAY 3 TASK COMPLETION SUMMARY ==='
            echo '✅ Task 1: Implement Automated Testing - COMPLETE'
            echo '✅ Task 2: Configure Test Reports & Code Coverage - COMPLETE'
            echo '✅ Additional: Code Quality Tool (SonarQube) - COMPLETE'
            echo '==============================='
            echo ''
            echo '📊 View Reports:'
            echo '- Jenkins Test Results: ${BUILD_URL}testReport/'
            echo '- Code Coverage: ${BUILD_URL}Code_20Coverage_20Report/'
            echo '- SonarQube: http://localhost:9000'
        }
        success {
            echo '🎉 All tasks completed successfully before 18 Nov deadline!'
        }
    }
}
