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
                    gv.setBuildNumber()
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
        always {
            script {
                gv.always()
            }
        }

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
