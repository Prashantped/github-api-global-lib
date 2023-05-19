def call(Map pipelineParams) {
pipeline {
    agent any
environment {
    IMAGE = 'https://hub.docker.com/_/${pipelineParams.node}'
}
stages {
        stage('Install dependencies') {
            steps {
                echo 'Installing dependencies'
                sh 'npm install'
            }
        }
        stage('Test') {
            steps {
                echo 'Linting'
                sh 'npm run lint'

                echo 'Testing'
                sh 'npm run test'
            }
        }
        stage('Build') {
            steps {
                sh "npm run build"
            }
        }
        stage('Dockerize') {
            parallel {
                stage('latest') {                    
                    steps {
                        sh """
                            docker build -t ${env.IMAGE} .
                            docker push ${env.IMAGE}
                        """
                    }
                }
}
        }
}
}
}
