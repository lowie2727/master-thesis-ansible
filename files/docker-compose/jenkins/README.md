# Jenkins

- [source code](https://github.com/jenkinsci/jenkins)
- [docker wiki](https://github.com/jenkinsci/docker#docker-compose-with-jenkins)
- [main site](https://www.jenkins.io/)

## Setup

Navigate to the web interface and check the log files for the initial admin password. To check the logs files run the following command:

```bash
docker logs jenkins
```

Install the suggested plugins.

### Using agents

Follow the following guide to setup an agent.

- [agents](https://www.jenkins.io/doc/book/using/using-agents/)

You can find the add credentials button at `Manage Jenkins > Credentials > System >Global credentials (unrestricted)`

When creating a node you can just follow the guide except for the host parameter. Just set the Host to `ssh-agent`.

Follow these [steps](https://www.jenkins.io/doc/book/using/using-agents/#delegating-the-first-job-to-agent1) to check if your setup is working.