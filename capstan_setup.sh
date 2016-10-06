#!/usr/bin/env bash

sudo apt-get update
sudo apt-get install curl
curl https://raw.githubusercontent.com/cloudius-systems/capstan/master/scripts/download | bash
#sudo apt-get install libedit-dev

sudo apt-get install gradle
sudo apt-get install default-jdk
sudo apt-get install qemu-system

sudo adduser $USER kvm

# You may need to log out and re log in for capstan to be a recognizable command after its installation,
# and group permissions to work.
