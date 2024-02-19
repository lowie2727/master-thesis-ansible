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

```bash
docker logs jenkins
```

After entering the initial admin password select install suggested plugins.

### Using agents

Follow the following guide to setup an agent.

- [agents](https://www.jenkins.io/doc/book/using/using-agents/)

You can find the add credentials button at `Manage Jenkins > Credentials > System > Global credentials (unrestricted)`

When creating a node you can just follow the guide except for the host parameter. Just set the Host to `ssh-agent`.

Follow these [steps](https://www.jenkins.io/doc/book/using/using-agents/#delegating-the-first-job-to-agent1) to check if your setup is working.

### Not building on the built-in node

It is [inadvisable](https://www.jenkins.io/doc/book/security/controller-isolation/#not-building-on-the-built-in-node) to build using the built-in node.

navigate to `Manage Jenkins > Nodes > Built-In Node > Configure` and set the `Number of executors` parameter to 0.
