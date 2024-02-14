FROM jenkins/ssh-agent:alpine-jdk17 as ssh-agent

# Install pip
RUN apk --no-cache add py3-pip

# Copy the key file
COPY --chown=jenkins mykey "${JENKINS_AGENT_HOME}"/.ssh/mykey
