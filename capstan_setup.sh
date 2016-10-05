#!/usr/bin/env bash

sudo apt-get update
sudo apt-get install curl
curl https://raw.githubusercontent.com/cloudius-systems/capstan/master/scripts/download | bash
#sudo apt-get install libedit-dev

sudo apt-get install gradle
sudo apt-get install default-jdk
