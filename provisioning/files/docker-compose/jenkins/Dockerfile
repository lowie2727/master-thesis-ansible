FROM jenkins/ssh-agent:alpine-jdk17 as ssh-agent

# Install pip
RUN apk --no-cache add py3-pip

ENV JENKINS_AGENT_HOME=/home/jenkins

# Copy the key file
COPY --chown=jenkins jenkins_agent_key.pub "${JENKINS_AGENT_HOME}"/.ssh/authorized_keys
