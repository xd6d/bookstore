# Bookstore

### Execution

#### Server

Set up gRPC Server with ``./gradlew -p server clean bootRun``. 

It starts gRPC Server at localhost:9090.

#### Client

Optionally, you can start REST client which will accept HTTP requests at localhost:8081, forward them to gRPC Server 
mentioned above and return you JSON responses.

``./gradlew -p client-rest clean bootRun``

You can find example requests and more information about client in `client-rest` folder

### Tests

There is SonarCloud workflow in this project which builds and runs tests, so generally there is no need to run tests locally.

And still you can run tests both at `server` and `client-rest` by ``./gradlew clean test``. 

Note: `server` tests use testcontainers, so be sure your Docker is running