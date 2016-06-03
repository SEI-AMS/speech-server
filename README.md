# speech-server
Test server: Speech Recognition Server based on Sphinx

speech-server is a test example of a server for speech recognition. After building speech-server it has to be added to the Cloudlet Server as a Service VM. Instructions for creating, adding and testing a Service VM are available at https://github.com/SEI-AMS/pycloud/wiki/Service-VMs.

speech-server is a basically a wrapper to the Sphinx speech recognition software developed at Carnegie Mellon University. The Sphinx libraries are required in order to build the test speech server.

The ./src/dist folder has all configuration and model files that need to be on the same folder as the speech-server executable for it to run properly.

Cloudlets are discoverable, generic, stateless servers located in single-hop proximity of mobile devices, that can operate in disconnected mode and are virtual-machine (VM) based to promote flexibility, mobility, scalability, and elasticity. In our implementation of cloudlets, applications are statically partitioned into a very thin client that runs on the mobile device and a computation-intensive Server that runs inside a Service VM. Read more about cloudlets at http://sei.cmu.edu/mobilecomputing/research/tactical-cloudlets/.

KD-Cloudlet comprises a total of 7 GitHub projects:

* pycloud (Cloudlet Server)

* cloudlet-client (Cloudlet Client)

* client-lib-android (Library for Cloudlet-Ready Apps)

* client-lib (Java REST Client Library)

* android-logger (SLF4J Logger for Android)

* speech-server (Test server: Speech Recognition Server based on Sphinx)

* speech-android (Test client: Speech Recognition Client)

Building and Installation information in https://github.com/SEI-AMS/pycloud/wiki.
