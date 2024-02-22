def gv

pipeline {
    agent {
        node {
            label 'agent1'
        }
    }

    stages {
        stage('init') {
            steps {
                script {
                    gv = load "script.groovy"
                }
            }
        }

        stage('checkout') {
            steps {
                script {
                    gv.checkout()
                }
            }
        }

        stage('run ansible-lint') {
            steps {
                script {
                    gv.ansible()
                }
            }
        }
    }

    post {
        success {
            script {
                gv.succes()
            }
        }

        failure {
            script {
                gv.failure()
            }
        }
    }
}
