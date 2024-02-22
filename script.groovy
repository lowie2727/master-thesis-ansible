def checkout() {
    checkout([$class: 'GitSCM', branches: [[name: '*/dev']], userRemoteConfigs: [[url: 'https://github.com/lowie2727/master-thesis-ansible.git']]])
}

def ansible() {
    sh 'ansible-lint provisioning/main.yml'
}

def succes() {
    echo 'Stage completed successfully'
    sh 'curl -d "The build was successful. Build number: ${BUILD_NUMBER}, Git commit: ${GIT_COMMIT}, Job URL: ${BUILD_URL}" ntfy.sh/lowievv'
}

def failure() {
    echo 'Stage failed'
    sh 'curl -d "The build was successful. Build number: ${BUILD_NUMBER}, Git commit: ${GIT_COMMIT}, Job URL: ${BUILD_URL}" ntfy.sh/lowievv'
}

return this
