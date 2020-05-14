# Run in Minikube

## Requirements
* [SBT](https://www.scala-sbt.org/download.html)
* [Docker](https://docs.docker.com/get-docker/)
* [Minikube](https://kubernetes.io/docs/setup/learning-environment/minikube/)
* [kubectl](https://kubernetes.io/docs/tasks/tools/install-kubectl/)

## Steps
1. Run `\$ minikube start` which bootstraps local Kubernetes cluster.
2. Enable Ingress so that you will be able to access your service
from outside of the cluster by running command `\$ minikube addons enable ingress`.
3. Attach current terminal session to Docker registry provided by minikube by `\$ eval \$(minikube docker-env)`
4. In the same terminal session Publish Docker image by running `\$ sbt docker:publishLocal`
5. Run `\$ kubectl create -f .` inside `k8s` directory.
6. Check what is the IP of your local cluster by `\$ minikube ip` and add new line
inside `/etc/hosts` which looks like following: `[minikube ip result] $ingress_host$`
7. Now you can access your service which runs in Kubernetes from the command line
by running `\$ curl -X GET -v http://$ingress_host$/guestbook` 

## Important notes
In this example publishing Docker image tagged with `latest` is disabled.
It is highly discouraged to use `latest` tag thus if you update your version
then you need to update version tag in Kubernetes definition in file `guestbook.yaml`.