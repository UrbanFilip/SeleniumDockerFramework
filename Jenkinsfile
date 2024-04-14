pipeline {

  agent any

  parameters {
    choice choices: ['chrome', 'firefox', 'edge'], description: 'Select browser', name: 'BROWSER'
  }

  stages {

      stage('Build Jar') {
        steps {
          bat "mvn clean package -DskipTests"
        }
      }

      stage('Start grid') {
        steps {
         bat "docker-compose -f grid.yaml up"
                }
              }

      stage('Run tests') {
        steps{
          bat "mvn test -Dbrowser=${params.BROWSER}"
        }
      }
  }

      post {
          always {
          bat "docker-compose -f grid.yaml down"
              script {
                  allure([
                          includeProperties: false,
                          jdk              : '',
                          properties       : [],
                          reportBuildPolicy: 'ALWAYS',
                          results          : [[path: 'allure-results']]
                  ])
              }
          }
      }
}