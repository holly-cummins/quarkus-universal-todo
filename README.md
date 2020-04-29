# Universal Quarkus Todo Application Quarkus

This project aims to explain a smooth migration path from a Spring Boot application to Quarkus.
It:

## Infrastructure service

Regardless of the runtime, the application uses PostgreSQL and Keycloak.
A `infrastructure/docker-compose` file starts these required services.
Run: 

```shell
docker-compose -f ./infratructure/docker-compose.yaml up
```

Once started (can take a few minutes), open http://localhost:8180/auth/admin/ and login with `admin/admin`.

## Applications

This repo provides 2 versions of the application:

* a Spring version
* a Quarkus version

### Spring Application

Build it with:

```bash
mvn -B clean verify -f spring/pom.xml
``` 

Run it with:

```bash
java -jar spring/target/universal-todo-spring-1.0.0-SNAPSHOT.jar 
``` 
                
### Quarkus Application

To build the Quarkus application in JVM mode, run:

```bash
mvn -B clean verify -f quarkus/pom.xml    
``` 

Run it with:

```bash
java -jar quarkus/target/universal-todo-spring-1.0.0-SNAPSHOT-runner.jar 
``` 

To build the native executable, run:

```bash
mvn -B clean verify -f quarkus/pom.xml -Pnative
```

Run the native-executable with:

```bash
quarkus/target/universal-todo-quarkus-1.0.0-SNAPSHOT-runner
```



       run:     