FROM jenkins/ssh-agent:alpine-jdk17 as ssh-agent

RUN apk --no-cache add ansible ansible-lint curl

ENV JENKINS_AGENT_HOME=/home/jenkins

# Copy the key file
COPY --chown=jenkins jenkins_agent_key.pub "${JENKINS_AGENT_HOME}"/.ssh/authorized_keys
