pipeline {
  agent any

  triggers {
    githubPush()
  }

  tools {
    maven 'Maven_3'
  }

  options {
    skipDefaultCheckout(true)
    timestamps()
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Build App') {
      steps {
        ansiColor('xterm') {
          // Run Maven from repo root (not app/)
          bat 'mvn -version'
          bat 'mvn -B clean package'
        }
      }
    }

    stage('Compose Up') {
      steps {
        ansiColor('xterm') {
          bat 'docker compose down || exit 0'
          bat 'docker rm -f selenium-hub || exit 0'
          bat 'docker rm -f springboot-app || exit 0'
          bat 'docker rm -f selenium-chrome || exit 0'
          bat 'docker network prune -f || exit 0'

          bat 'docker compose up --build -d'

          // wait until app is healthy instead of blind sleep
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

    stage('Run UI Tests (TestNG)') {
      steps {
        ansiColor('xterm') {
          // Run tests from repo root or adjust if tests/ has its own pom.xml
          bat 'mvn -B test'
        }
      }
    }
  }

  post {
    always {
      ansiColor('xterm') {
        bat 'docker compose down'
        archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true

        // Publish TestNG results using JUnit-compatible XMLs
        junit '**/target/surefire-reports/*.xml'

        // Optional: publish HTML TestNG report if generated
        publishHTML([reportDir: 'target/surefire-reports',
                     reportFiles: 'index.html',
                     reportName: 'TestNG Report'])
      }
    }
  }
}
