# Jenkins

- [source code](https://github.com/jenkinsci/jenkins)
- [docker wiki](https://github.com/jenkinsci/docker#docker-compose-with-jenkins)
- [main site](https://www.jenkins.io/)

## Setup

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

### Create custom agent docker image

Modify the original [docker-compose](/files/docker-compose/jenkins/docker-compose.yml) file to the following:

```yaml
  ssh-agent:
    container_name: jenkins-ssh-agent
    build:
      context: .
      dockerfile: Dockerfile
    restart: always
```

This will build the [Dockerfile](/files/docker-compose/jenkins/Dockerfile) with custom packages included. Make sure the public keyfile is in the same place as the Dockerfile.
