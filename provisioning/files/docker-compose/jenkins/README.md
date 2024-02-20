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

After entering the initial admin password select install suggested plugins.

The suggested plugins are:

- [Ant Plugin](https://plugins.jenkins.io/ant)
- [Build Timeout](https://plugins.jenkins.io/build-timeout)
- [Credentials Binding Plugin](https://plugins.jenkins.io/credentials-binding)
- [Email Extension Plugin](https://plugins.jenkins.io/email-ext)
- [Folders Plugin](https://plugins.jenkins.io/cloudbees-folder)
- [Git plugin](https://plugins.jenkins.io/git)
- [GitHub Branch Source Plugin](https://plugins.jenkins.io/github-branch-source)
- [Gradle Plugin](https://plugins.jenkins.io/gradle)
- [JavaMail API](https://plugins.jenkins.io/javax-mail-api)
- [LDAP Plugin](https://plugins.jenkins.io/ldap)
- [Matrix Authorization Strategy Plugin](https://plugins.jenkins.io/matrix-auth)
- [OWASP Markup Formatter Plugin](https://plugins.jenkins.io/antisamy-markup-formatter)
- [PAM Authentication plugin](https://plugins.jenkins.io/pam-auth)
- [Pipeline](https://plugins.jenkins.io/workflow-aggregator)
- [Pipeline: GitHub Groovy Libraries](https://plugins.jenkins.io/pipeline-github-lib)
- [Pipeline: Stage View Plugin](https://plugins.jenkins.io/pipeline-stage-view)
- [SSH Build Agents plugin](https://plugins.jenkins.io/ssh-slaves)
- [Timestamper](https://plugins.jenkins.io/timestamper)
- [Workspace Cleanup Plugin](https://plugins.jenkins.io/ws-cleanup)

### Using agents

Follow the following guide to setup an agent.

- [agents](https://www.jenkins.io/doc/book/using/using-agents/)

You can find the add credentials button at `Manage Jenkins > Credentials > System > Global credentials (unrestricted)`. The public and private key can be found in the `/home/your_username/appdata/jenkins` folder on your Jenkins server.

You can skip the agent1 creation because it should already by present in Jenkins but it doesn't always work. It's the same as in the guide except for the host parameter. The host parameter is set `ssh-agent` because of Docker networking.

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
