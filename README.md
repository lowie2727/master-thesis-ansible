# Master thesis

## Debian server setup

### Setup SSH key

#### Step 1

Generate a key pair on the host machine with the ed25519 algorithm.

```bash
ssh-keygen -t ed25519 -C "name-of-host"
```

The generated key is in a name and location of your choice.

#### Step 2

Copy the public key to the target machine. The key file is in the folder you just selected.

```bash
ssh-copy-id -i /path/to/keyfile.pub user@target
```

You will be promted for the user password. After the succesfull transfer you should add a SSH configuration to make it easier to login. change this text!!!! should be on host machine not Jenkins server

```
Host jenkins
  Hostname jenkins-server-ip
  User username
  IdentityFile /path/to/private_key_file
```

#### Step 3

For security reasons, it is better to disable password-based authentication on the Jenkins server  machine.

The configuration is located in the following file:

```bash
sudo nano /etc/ssh/sshd_config
```

Uncomment and change the following line (yes to no).

```bash
PasswordAuthentication no
```

Finally, restart the SSH server.

```bash
sudo systemctl restart sshd
```

To SSH into the Jenkins server simply type:

```
ssh jenkins
```

### Installing Ansible

Follow this [guide](https://docs.ansible.com/ansible/latest/installation_guide/intro_installation.html) to install ansible on the host machine.

#### Ping the Jenkins server

Add an inventory file named inventory.ini to the top folder of your Ansible directory on your host machine. The content of the inventory file should look like this:

```ini
[jenkins_server]
jenkins ansible_host=jenkins-server-ip ansible_user=username ansible_become_password=sudo-password ansible_private_key_file=/path/to/private_key_file
```

If the inventory file is configuered correctly the following command should work:

```
ansible -i inventory.ini jenkins -m ping
```

#### Deploy playbook

To deploy the [main.yml](playbooks/main.yml) playbook use the following command:

```
ansible-playbook playbooks/main.yml
```
