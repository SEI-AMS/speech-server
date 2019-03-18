#!/usr/bin/env bash

sudo apt-get update

# Capstan dependencies.
sudo apt-get -y install qemu-system

# Depdencies for creating the Speech Server itself.
sudo apt-get -y install gradle
sudo apt-get -y install default-jdk

# Capstan installation.
sudo apt-get -y install curl
curl https://raw.githubusercontent.com/cloudius-systems/capstan/master/scripts/download | bash

sudo adduser $USER kvm

# You may need to log out and re log in for capstan to be a recognizable command after its installation,
# and group permissions to work.
