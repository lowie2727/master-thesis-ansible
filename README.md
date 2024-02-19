# Master thesis

## Vagrant

To make it easier to test the Ansible playbook on virtual machines, [Vagrant](https://developer.hashicorp.com/vagrant) is used.

If you want to use Ansible separately from Vagrant on a Debian server, you can read [this](/provisioning/README.md) explanation.

### Setup

First, install vagrant via the following [site](https://developer.hashicorp.com/vagrant/install) for the appropriate operating system.

In this case, libvirt was used to manage the virtual machines, but there are [other options](https://developer.hashicorp.com/vagrant/docs/providers). The following [link](https://opensource.com/article/21/10/vagrant-libvirt) explains how to set up libvirt.

All Vagrant settings can be found in the [Vagrantfile](/Vagrantfile). More info on the Vagrantfile can be found [here](https://developer.hashicorp.com/vagrant/docs/vagrantfile).

### Useful Vagrant commands

[Link](https://developer.hashicorp.com/vagrant/docs/cli) to the documentation.

```bash
vagrant up
```

Creates and configures guest machines according to your Vagrantfile.

```bash
vagrant halt
```

Stops the running VM.

```bash
vagrant destroy
```

Stops the running VM and destroys all resources that were created during the VM creation process.

```bash
vagrant ssh
```

Connects to the VM via SSH.

```bash
vagrant provision
```

Runs the configured provisioners on the VM.

```bash
vagrant reload
```

Reloads the VM, applying any changes to the Vagrantfile.
