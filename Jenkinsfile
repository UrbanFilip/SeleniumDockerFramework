pipeline {

  agent any

  options {
    skipDefaultCheckout(true)
  }

  parameters {
    choice choices: ['chrome', 'firefox', 'edge'], description: 'Select browser', name: 'BROWSER'
    choice choices: ['qa', 'staging', 'prod'],     description: 'Select environment', name: 'ENVIRONMENT'
    string name: 'SUITE', defaultValue: 'testng.xml', description: 'Path to TestNG suite file (e.g. testng.xml or suites/smoke.xml)'
  }

  stages {

    stage('Clean workspace') {
      steps {
        cleanWs()
        checkout scm
      }
    }

    stage('Build Jar') {
      steps {
        bat "mvn clean package -DskipTests"
      }
    }

    stage('Start grid') {
      steps {
        script {
          def envFile = "${params.BROWSER}.env"
          echo "Using env file: ${envFile}"

          bat "docker compose --env-file ${envFile} -f grid.yaml pull"
          bat "docker compose --env-file ${envFile} -f grid.yaml down || ver > nul"
          bat "docker compose --env-file ${envFile} -f grid.yaml up -d"
        }
      }
    }

   stage('Wait for Grid') {
     steps {
       withEnv(['GRID_URL=http://localhost:4444/status']) {
         bat "java -cp target/classes org.selenium.GridHealthCheck %GRID_URL% 90 1000"
       }
     }
   }


     stage('Validate suite file') {
          steps {
            script {
              def firstSuite = params.SUITE.tokenize(',')[0].trim()
              bat """
              @echo off
              if not exist "${firstSuite}" (
                echo [ERROR] Suite file not found: ${firstSuite}
                exit /b 3
              ) else (
                echo [OK] Suite file found: ${firstSuite}
              )
              """
            }
          }
        }

    stage('Run tests') {
      steps {
        bat "mvn test -Dsurefire.suiteXmlFiles=${params.SUITE} -Dbrowser=${params.BROWSER} -Dremote=true -Denv=${params.ENVIRONMENT}"
      }
    }
  }

  post {
    always {
      script {
        def envFile = "${params.BROWSER}.env"
        bat "docker compose --env-file ${envFile} -f grid.yaml down"
      }
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
