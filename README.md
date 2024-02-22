# Master thesis

> [!IMPORTANT]
> If you're looking for more details on the Jenkins server deployment and configuration, you can find them [here](/provisioning/files/docker-compose/jenkins/).

## Vagrant

To make it easier to test the Ansible playbook on virtual machines, [Vagrant](https://developer.hashicorp.com/vagrant) is used.

If you want to use Ansible separately from Vagrant on a Debian server, you can read [this](/provisioning/README.md#installing-ansible) explanation.

### Setup

> [!TIP]
> If you haven't installed ansible yet, you can follow the following [explanation](/provisioning/README.md#installing-ansible).

First, install vagrant via the following [site](https://developer.hashicorp.com/vagrant/install) for the appropriate operating system.

#### Libvirt

In this case, libvirt was used to manage the virtual machines, but there are [other options](https://developer.hashicorp.com/vagrant/docs/providers). The following [link](https://opensource.com/article/21/10/vagrant-libvirt) explains how to set up libvirt.

In order prevent libvirt from asking for the root password simply run the following commands:

```zsh
sudo usermod -a -G libvirt $(whoami)
sudo usermod -a -G kvm $(whoami)
```

You can also download a GUI for the virtual machines on Fedora:

```zsh
sudo dnf install virt-manager
```

Or Debian

```zsh
sudo apt install virt-manager
```

#### Vagrantfile

All Vagrant settings can be found in the [Vagrantfile](/Vagrantfile). More info on the Vagrantfile can be found [here](https://developer.hashicorp.com/vagrant/docs/vagrantfile).

### Useful Vagrant commands

[Link](https://developer.hashicorp.com/vagrant/docs/cli) to the documentation.

```zsh
vagrant up
```

Creates and configures guest machines according to your Vagrantfile.

```zsh
vagrant halt
```

Stops the running VM.

```zsh
vagrant destroy
```

Stops the running VM and destroys all resources that were created during the VM creation process.

```zsh
vagrant ssh
```

Connects to the VM via SSH.

```zsh
vagrant provision
```

Runs the configured provisioners on the VM.

```zsh
vagrant reload
```

Reloads the VM, applying any changes to the Vagrantfile.
