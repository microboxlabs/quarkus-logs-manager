# quarkus-logs-manager

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## Application requirements
- Docker Compose version v2.29.7-desktop.1
- Java 21.0.5
- Apache Maven 3.8.8
- PostgreSQL 15


## Running the application
```shell script
./run.sh
```

## SSE
To stream the logs using SSE approach you need to connect you application to *http://localhost:8080/stream/logs/subscribe*
```shell script
curl -X POST http://localhost:8080/stream/logs/subscribe
```
Then, you can stream each log event emit over the event name *log*

```json
: Event type is logs
event: message
id: df2c073d-3031-4e7a-bf59-5b1bfecc09ed
retry: 3000
data: {
data:   "id": "df2c073d-3031-4e7a-bf59-5b1bfecc09ed",
data:   "eventCode": "logs",
data:   "eventTime": "Dec 2, 2024, 10:06:44 PM",
data:   "payload": {
data:     "id": 0,
data:     "timestamp": "2024-12-02 20:26:56",
data:     "logLevel": "INFO",
data:     "serviceName": "Service A",
data:     "message": "Error communicate the database"
data:   }
data: }

```
## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/quarkus-logs-manager-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

## Related Guides

- REST ([guide](https://quarkus.io/guides/rest)): A Jakarta REST implementation utilizing build time processing and Vert.x. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it.

## Provided Code

### REST

Easily start your REST Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)
