# Universal Quarkus Todo Application

This project uses Quarkus, the Supersonic Subatomic Java Framework.
It:

* has a REST API
* uses Panache with a PostGreSQL database
* use JWT-based security
* use Qute as template engine 

The frontend uses Vue.js.

## Prerequisites

The application uses PostGreSQL and Keycloak.
A `docker-compose` file starts these required services.
Run: 

```shell
docker-compose up
```

Once started (can take a few minutes), open http://localhost:8180/auth/admin/ and login with `admin/admin`.
Go to _master_ and click on `Add realm`.
Select the `quarkus_realms.json` file contained at the root of this repository.
Click on `Create`.

This realm defines 3 users:

* `daniel`/`daniel`
* `alice`/`alice`
* `clement`/ `clement`

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```
./mvnw quarkus:dev
```

Open http://localhost:8080 and log in with one of the user mentioned above.

## Packaging and running the application

The application is packageable using `./mvnw package`.
It produces the executable `universal-todo-1.0.0-SNAPSHOT-runner.jar` file in `/target` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

The application is now runnable using `java -jar target/universal-todo-1.0.0-SNAPSHOT-runner.jar`.

## Creating a native executable

You can create a native executable using: `./mvnw package -Pnative`.

Or you can use Docker to build the native executable using: `./mvnw package -Pnative -Dquarkus.native.container-build=true`.

You can then execute your binary: `./target/universal-todo-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/building-native-image-guide .