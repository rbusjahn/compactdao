pipeline {
  agent any
  stages {
    stage('error') {
      steps {
        sh 'mvn clean test'
        timestamps()
      }
    }
  }
}