pipeline {

  agent any

  parameters {
    choice choices: ['chrome', 'firefox', 'edge'], description: 'Select browser', name: 'BROWSER'
    choice choices: ['qa', 'staging', 'prod'], description: 'Select environment', name: 'ENVIRONMENT'
  }

  stages {

      stage('Build Jar') {
        steps {
          bat "mvn clean package -DskipTests"
        }
      }

      stage('Start grid') {
        steps {
         bat "docker-compose -f grid.yaml up -d"
        }
      }

      stage('Run tests') {
        steps{
          bat "mvn test -Dbrowser=${params.BROWSER} -Dremote=true -Denv=${params.ENVIRONMENT}"
        }
      }
  }

      post {
          always {
          bat "docker-compose -f grid.yaml down"
          }
      }
}