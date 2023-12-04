# Containerizing the Spring Boot Application

This guide walks you through the process of containerizing the Spring Boot application using Docker.

## Prerequisites

Before you begin, ensure you have the following installed on your system:

- Java 21
- Maven
- Docker

## Building the Application

1. Navigate to the root directory of your project.
2. Run the following command to build your application:

   ```shell
   ./mvnw clean package
   ```

   This will create a runnable jar file in the `target` directory of your project.

## Building the Docker Image

1. From the directory containing your Dockerfile, run:

   ```shell
   docker build -t crypt-recommendation-service .
   ```

   This command builds the Docker image and tags it as `crypo-recommendation-service`.

## Running the Docker Container

After the image is built, you can run it:

```shell
docker run -p 8080:8080 crypto-recommendation-service
```

This will make your application accessible at `http://localhost:8080`.

## Stopping and Removing the Docker Container

To stop the running container:

```shell
docker stop crypto-recommendation-service
```

To remove the container after stopping:

```shell
docker rm crypto-recommendation-service
```

List all containers (both running and stopped):

```shell
docker ls -a
```