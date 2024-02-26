# Jenkins

- [source code](https://github.com/jenkinsci/jenkins)
- [docker wiki](https://github.com/jenkinsci/docker#docker-compose-with-jenkins)
- [main site](https://www.jenkins.io/)

## Setup

### Create custom agent docker image

This Ansible playbook will use a custom docker image by default.

```yaml
  ssh-agent:
    container_name: jenkins-ssh-agent
    build:
      context: .
      dockerfile: Dockerfile
    restart: always
```

This will build the [Dockerfile](/files/docker-compose/jenkins/Dockerfile) with custom packages included. The public key will be automaticly generated on the target machine and will be available in the `/home/username_variable/appdata/jenkins` folder.

### Using default docker image

If you don't want any custom packages in your ssh-agent you can modify the [docker-compose](/files/docker-compose/jenkins/docker-compose.yml) to the following:

```yaml
  ssh-agent:
    image: jenkins/ssh-agent:latest-alpine-jdk17
    container_name: jenkins-ssh-agent
    environment:
      - JENKINS_AGENT_SSH_PUBKEY=<public_key>
    restart: always
```

This will use the provided docker image by Jenkins. Make sure to paste your public key in the designated spot.

### Web interface

Navigate to the web interface and check the log files for the initial admin password. To check the logs files run the following command on the Jenkins server:

```zsh
docker logs jenkins
```

After entering the initial admin password select `Select plugins to install`.

The suggested plugins are:

- [Folders Plugin](https://plugins.jenkins.io/cloudbees-folder)
- [OWASP Markup Formatter Plugin](https://plugins.jenkins.io/antisamy-markup-formatter)
- [Build Timeout](https://plugins.jenkins.io/build-timeout)
- [Credentials Binding Plugin](https://plugins.jenkins.io/credentials-binding)
- [Timestamper](https://plugins.jenkins.io/timestamper)
- [Workspace Cleanup Plugin](https://plugins.jenkins.io/ws-cleanup)
- [Gradle Plugin](https://plugins.jenkins.io/gradle)
- [Pipeline](https://plugins.jenkins.io/workflow-aggregator)
- [GitHub Branch Source Plugin](https://plugins.jenkins.io/github-branch-source)
- [Pipeline: GitHub Groovy Libraries](https://plugins.jenkins.io/pipeline-github-lib)
- [Pipeline: Stage View Plugin](https://plugins.jenkins.io/pipeline-stage-view)
- [Git plugin](https://plugins.jenkins.io/git)
- [SSH Build Agents plugin](https://plugins.jenkins.io/ssh-slaves)
- [Matrix Authorization Strategy Plugin](https://plugins.jenkins.io/matrix-auth)
- [PAM Authentication plugin](https://plugins.jenkins.io/pam-auth)
- [LDAP Plugin](https://plugins.jenkins.io/ldap)
- [Email Extension Plugin](https://plugins.jenkins.io/email-ext)
- [Mailer](https://plugins.jenkins.io/mailer)
- [Dark Theme](https://plugins.jenkins.io/dark-theme/)

> [!TIP]
> Other useful plugins not included by default are:
> - [Build Monitor View](https://plugins.jenkins.io/build-monitor-plugin/)
> - [Build Name and Description Setter](https://plugins.jenkins.io/build-name-setter/)
> - [Embeddable Build Status](https://plugins.jenkins.io/embeddable-build-status/)
> - [GitHub](https://plugins.jenkins.io/github/)
> - [Pipeline Graph View](https://plugins.jenkins.io/pipeline-graph-view/)

### Using agents

#### Docker agent

Follow the following guide to setup an agent using Docker.

- [agents](https://www.jenkins.io/doc/book/using/using-agents/)

You can find the add credentials button at `Manage Jenkins > Credentials > System > Global credentials (unrestricted)`. The public and private key can be found in the `/home/your_username/appdata/jenkins` folder on your Jenkins server.

You can skip the agent1 creation because it should already by present in Jenkins but it doesn't always work. It's the same as in the guide except for the host parameter. The host parameter is set `ssh-agent` because of Docker networking.

#### Windows agent

If you want a Jenkins windows agent you will have to install the [Java JDK 17](https://www.oracle.com/java/technologies/downloads/#jdk17-windows) first. You also need [openSSH](https://learn.microsoft.com/en-us/windows-server/administration/openssh/openssh_install_firstuse?tabs=powershell) client and server to connect to the main Jenkins server.

You can follow this [guide](https://learn.microsoft.com/en-us/windows-server/administration/openssh/openssh_keymanagement) to generate the public and private key pair. If you're an administrator on the Windows machine, make sure your public key is stored in `C:\ProgramData\ssh\administrators_authorized_keys`. You can read more about it [here](https://learn.microsoft.com/en-us/windows-server/administration/openssh/openssh_keymanagement#administrative-user). The private key should be stored in the Jenkins server under the credentials tab.

To add a node go to `Dashboard` > `Manage Jenkins` > `Nodes` and click on `New Node`. Name it `windows-agent` and create it. Set the `Remote root directory` to `c:\jenkins\agents`. Set the `Labels` parameter to `windows-agent`. Set the `Launch method` to `Launch agents via SSH`. The `Host` parameter should be set to the ip of the windows machine. Add the ssh credentials for your windows machine and make sure to set `Host Key Verification Strategy` to `Manually trusted key Verification Strategy`.

### First job

Follow these [steps](https://www.jenkins.io/doc/book/using/using-agents/#delegating-the-first-job-to-agent1) to check if your setup is working.

### Not building on the built-in node

It is [inadvisable](https://www.jenkins.io/doc/book/security/controller-isolation/#not-building-on-the-built-in-node) to build using the built-in node.

navigate to `Manage Jenkins > Nodes > Built-In Node > Configure` and set the `Number of executors` parameter to 0.

## Create nightly builds pipeline

From the main screen, click `New Item`, enter a valid name and select the `Pipeline` option.

Under the `General` section, select the `GitHub project` box and enter the `Project url`.

Under the heading `Build Triggers`, select `Build periodically`. In the `Schedule` section you can setup your build schedule with [cron](https://en.wikipedia.org/wiki/Cron) syntax.

```zsh
TZ=Europe/Brussels
0 0 * * *
```

This configuration would run every day on midnight in the Europe/Brussels time zone. However, this is not ideal for continuously building software projects.

A simple pipeline for echoing could look like this:

```groovy
pipeline {
    agent any
    stages {
        stage('build') {
            steps {
                echo 'Building...'
            }
        }
    }
}
```

## Create continuous integration pipeline

> [!IMPORTANT]
> For this to work your Jenkins server should be exposed to the internet. You can expose it using your own domain name or use a tool like [ngrok](https://ngrok.com/) to expose your local environment.

First of all, we need to create a GitHub personal access token. Navigate to [https://github.com/settings/tokens](https://github.com/settings/tokens) and generate a new classic token. Name this token something like jenkins and select an expiration period. Make sure the token has the `admin:org_hook` scope enabled. Generate the token and make sure to copy your personal access token. Once you copied your token go back to the jenkins dashboard and navigate to `Manage Jenkins` > `Credentials` > `Global` and click and `Add Credientials`. Select `Secret text` in the dropdown menu and paste your copied key in the `Secret` area. You can name `ID` something like github-pat, don't forget to click create.

Next navigate back to the `dashboard` > `Manage Jenkins` > `System` and find the `GitHub` section. Add GitHub Server, name it like you want. You can leave the `API URL` default (https://api.github.com) and add your github-pat secret text file under the `Credentials` tab. You can test your credentials using a button, it should not fail if you did everythin correct. Also enable the `Manage hooks` checkbox. Make sure to save your changes before going to the next step.

The next step is creating the pipeline. On your dashboard click on `New Item`, name it and select the `Pipeline` option. Select the `GitHub project` checkbox and fill in your GitHub project url like https://github.com/username/reponame. Select the `GitHub hook trigger for GITScm polling` as build trigger. Finally enter the following pipeline:

```groovy
pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/main']], userRemoteConfigs: [[url: 'https://github.com/lowie2727/master-thesis-ansible.git']]])
            }
        }

        stage('Build') {
            steps {
                echo "Hello world!"
            }
        }
    }
}
```

> [!WARNING]
> Be sure to build this manually at least once to make sure it works properly.

## Ansible lint checking of this repository with notification

First you have to modify the [Dockerfile](Dockerfile) to include `ansible`, `ansible-lint` and `curl`. Include the following line in the Dockerfile:

```Dockerfile
RUN apk --no-cache add ansible ansible-lint curl
```

To run ansible-lint run the following pipeline:

```groovy
pipeline {
    agent {
        node {
            label 'agent1'
        }
    }

    stages {
        stage('Checkout') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/dev']], userRemoteConfigs: [[url: 'https://github.com/lowie2727/master-thesis-ansible.git']]])
            }
        }

        stage('Run ansible-lint') {
            steps {
                sh 'ansible-lint provisioning/main.yml'
            }
        }
    }

    post {
        success {
            echo 'Stage completed successfully'
            sh 'curl -d "The build was successful. Build number: ${BUILD_NUMBER}, Git commit: ${GIT_COMMIT}, Job URL: ${BUILD_URL}" ntfy.sh/mytopic'
        }

        failure {
            echo 'Stage failed'
            sh 'curl -d "The build was successful. Build number: ${BUILD_NUMBER}, Git commit: ${GIT_COMMIT}, Job URL: ${BUILD_URL}" ntfy.sh/mytopic'
        }
    }
}
```

> [!NOTE]
> This pipeline first checks out this repository and afterwards runs the ansible-lint command. The final step is sending a notification to a [ntfy](https://ntfy.sh/) topic.

## Pipeline script from SCM

If you want the pipeline script to be included in the repository itself, you can select the `Pipeline script from SCM option` instead of the `Pipeline script` option. The configuration options are very straight forward.

> [!NOTE]
> The pipeline configurations can be found in the [Jenkinsfile](/Jenkinsfile) and the [script.groovy](/script.groovy) file for this repository.

## Using a private repository

If you are using a private repository, you can put the following line in your groovy script:

```groovy
checkout scmGit(branches: [[name: '*/main']],
                userRemoteConfigs: [[credentialsId: 'your-credential-id-here', url: 'git@github.com:username/reponame.git']])
```

You can generate a key pair using the following command:

```zsh
ssh-keygen -t ed25519 -C "your-comment-here"
```

If you only want to create acces for a single repository navigate to your repository settings in GitHub and go to the `Deploy keys` section and click on `Add deploy key`. Paste your public key in the designated place. Don't forget to add your private key in the Jenkins credentials.

> [!IMPORTANT]
> If you get the following error message `stderr: No ED25519 host key is known for github.com and you have requested strict checking.` in the output logs, then you can temporarily set the `Git Host Key Verification Configuration` to `Accept first connection`.
