pipeline {
  agent any
  stages {
    stage('Test') {
      steps {
        git 'https://github.com/agntdv/data-source-routing'
      }
    }

    stage('Build') {
      steps {
        withGradle()
      }
    }

  }
}