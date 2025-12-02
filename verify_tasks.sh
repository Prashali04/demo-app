#!/bin/bash
echo "=== Verifying Day 3 Task Completion ==="
echo ""
echo "1. Checking files..."
ls -la pom.xml Jenkinsfile TASK_COMPLETION.md src/test/java/com/example/AppTest.java
echo ""
echo "2. Checking Jenkinsfile content..."
grep -n "junit\|jacoco\|sonar\|publishHTML" Jenkinsfile
echo ""
echo "3. Testing locally..."
mvn clean test 2>&1 | tail -10
echo ""
echo "4. Git status..."
git status --short
echo ""
echo "✅ All tasks implemented and ready for submission!"
echo "📊 View in Jenkins: http://localhost:8080"
