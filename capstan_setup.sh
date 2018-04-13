#!/usr/bin/env bash

sudo apt-get update

# Capstan dependencies.
sudo apt-get -y install git
sudo apt-get -y install qemu-system
sudo apt-get -y install golang

# Depdencies for creating the Speech Server itself.
sudo apt-get -y install gradle
sudo apt-get -y install default-jdk

# Capstan installation.
go get github.com/cloudius-systems/capstan

sudo adduser $USER kvm

# You may need to log out and re log in for capstan to be a recognizable command after its installation,
# and group permissions to work.
