# Kubernetes Akka HTTP Scala g8 seed

Prerequisites:
- JDK 8
- sbt 0.13.13 or higher

Open a console and run the following command to apply this template:
 ```
sbt -Dsbt.version=1.3.10 new VirtusLab/akka-http-kubernetes.g8
 ```

This template will prompt for the following parameters. Press `Enter` if the default values suit you:
- `name`: Becomes the name of the project.
- `scala_version`: Specifies the Scala version for this project.
- `akka_http_version`: Specifies which version of Akka HTTP should be used for this project.
- `akka_version`: Specifies which version of Akka should be used for this project. Must be at least 2.6.0.
- `organization`: Specifies the organization for this project.

There are also additional parameters which are calculated automatically based on input above.
Usually default values are suitable so do not change them unless there is special need for that.
Those parameters are:
- `normalized_name`: Normalized name used in Kubernetes description files.
- `snake_name`: Parameter used in Kuberenetes description file inside labels for both `Service` and `Deployment` definitions.
- `ingress_host`: Ingress host name.
- `package`: Root package for the entire project.

Once inside the project folder check README.md file there.