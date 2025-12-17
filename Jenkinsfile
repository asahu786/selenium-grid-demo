stage('Compose Up') {
  steps {
    ansiColor('xterm') {
      // free port 4444 if occupied
      bat 'for /f "tokens=5" %%a in (\'netstat -ano ^| findstr :4444\') do taskkill /PID %%a /F'

      bat 'docker compose down || exit 0'
      bat 'docker rm -f selenium-hub || exit 0'
      bat 'docker rm -f springboot-app || exit 0'
      bat 'docker rm -f selenium-chrome || exit 0'
      bat 'docker network prune -f || exit 0'

      bat 'docker compose up --build -d'

      // wait until app is healthy
      bat '''
      for /l %%x in (1, 1, 40) do (
        curl -s http://localhost:8089/actuator/health && exit 0
        timeout /t 2 >nul
      )
      exit 1
      '''
    }
  }
}

post {
  always {
    ansiColor('xterm') {
      bat 'docker compose down'
      archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
      junit '**/target/surefire-reports/*.xml'
      // publishHTML works only if plugin is installed
    }
  }
}
