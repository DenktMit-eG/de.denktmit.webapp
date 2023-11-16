# DenktMit eG - Webapp - Spring Boot 3.1+ development base
This is the default development base for DenktMit eG Webapps. It is set up
as three-tier architecture with a persistence, a business and web layer 
for server-side HTML, as well as JSON and XML rendering

## Already included and Roadmap
**This repository already provides**

* Robust's Maven setup, which allows developers to get started right away.
* Includes Maven Flyway configuration for database filling
* Externalization of credentials via .env files with default to .env-local
* Profile-oriented Docker-compose setup to provide external dependencies 
  for local integration tests
* Reusable integration test data that allows simple unit instead of 
  integration tests above the persistence layer
* Modular Spring configuration setup
* Preconfigured Spring security setup for RBAC
* UserDetailService for RBAC
* SpringContext hanger for integration tests and first persistence integration 
  tests
* JaCoCo CodeCoverage reporting, separated by unit and integration tests as 
  well as aggregated
* Asciidoc Setup for documentation close to the code
* Selenium Hub prepared for E2E tests
* Documentation

**We have already implemented the following features several times in different 
projects, so that they will also find their way into this repository in the 
future**

* E2E tests via JBehave or Gauge, which perform black-box integration tests 
  using the Selenium Hub
* Creation of deliverable test containers for E2E testing in the field. See 
[also my blog article on testcontainers](https://denktmit.de/blog/2021/11/21/release-and-ship-your-e2e-tests-as-containers-alongside-your-product/)
* Provision of [every HTML/CSS layout basics](https://every-layout.dev/)
* Complete RBAC management in the UI
* OAuth2 / OpenID Connect integration with Keycloak, switchable via Spring 
  Config Flag
* CI Build Samples for ConcourseCI, Github, Gitlab and Jetbrains Space
* Integration of fully tested own JavaScripts via Webjar module
* HATEOAS enabled REST API implementing [JSON Hypertext Application language](https://datatracker.ietf.org/doc/html/draft-kelly-json-hal-11)
* OpenAPI for REST
* A second UI layer but in Wicket 10 with plenty of Kotlin Goodies

## Local development
Please note, that any paths used in this documentation are relative to this
projects root directory

### Requirements
The following runtimes and tools are required to build and run this software:

| Requirements   | Version                       | Installation                                                                    |
|----------------|-------------------------------|---------------------------------------------------------------------------------|
| Docker         | latest                        | See [Docker](https://docs.docker.com/engine/install)                            |
| Docker-Compose | `2.x` (`2.21.0`)              | See [Docker](https://docs.docker.com/engine/install)                            |
| Java JDK       | `17.x` (currently `17.0.8.1`) | See [Eclipse Temurin JDK](https://adoptium.net/de/temurin/releases/?version=19) |
| Maven          | `3.x` (currently `3.9.5`)     | See [Apache Maven](https://maven.apache.org/install.html)                       |

Notice, that instead of installing Maven, you can also make use of the 
Maven wrapper provided within this repository. Just run

```bash
./mvnw clean install
```

inside the root repository of this project

### Build and run the application
Basically you will have to ensure, that you service dependencies are [README.md](README.md)
available. Start them with docker-compose and then build with maven
and start the application

#### Start service dependencies provided by docker-compose 
This repository is accompanied by a **docker-compose.dev.yaml** file. It 
provides single goto requirement for the build dependencies. Just make
sure to have it started before building the project

```bash
COMPOSE_PROFILES=dev,it docker compose --env-file .env-dev -f docker-compose.dev.yaml up -d
```

Once you are done with it, you can just stop it, so it does not use up
your system resources

```bash
COMPOSE_PROFILES=dev,it docker compose  --env-file .env-dev -f docker-compose.dev.yaml stop
```

and restart it, when you need it again

```bash
COMPOSE_PROFILES=dev,it docker compose  --env-file .env-dev -f docker-compose.dev.yaml --profile dev start
```

In case you need a fresh start, you can tell docker-compose to tear down
everything and start anew.

```bash
# Read the .env file into environment variables
ENV_FILE="$(pwd)/.env-dev"
source ${ENV_FILE}
# Tear down docker containers and remove volumes
COMPOSE_PROFILES=dev,it,e2e docker compose  --env-file .env-dev -f docker-compose.dev.yaml down
# Remove the persistent docker volumes
docker volume rm ${COMPOSE_PROJECT_NAME}_db-dev-data ${COMPOSE_PROJECT_NAME}_db-it-data
```

#### Prepare the database with testdata
The pom.xml file of the persistence module defines Flyway executions to
fill the dev and it databases with testdata.

Fill dev database with testdata
```bash
./mvnw flyway:migrate@fill-dev -Dflyway.configFiles=../.flyway.dev.conf -f ./persistence/pom.xml
```

Fill integration test database with testdata (done automatically in maven build)
```bash
./mvnw flyway:migrate@fill-it -Dflyway.configFiles=../.flyway.it.conf -f ./persistence/pom.xml
```

#### Build the application
Make sure, your service dependencies are running. Then you can just run
the build with

```bash
./mvnw clean install
```

#### Start the application
Visit [http://localhost:8080](http://localhost:8080) to open the ui page

## End-to-end testing with Selenium
The simple webapp comes with full end-to-end testing features, based on
Selenium Hub and a set of browsers. 

To start Selenium hub within existing docker webapp context type

```bash
COMPOSE_PROFILES=e2e docker compose --env-file .env-dev -f docker-compose.dev.yaml up -d
```

And to stop it again issue an

```bash
COMPOSE_PROFILES=e2e docker compose  --env-file .env-dev -f docker-compose.dev.yaml down
```

## Secrets
Credentials for your docker dev environment and app config are in **.env**.

Optionally, if you use the flyway maven goals to load test data into your db, adjust
**.flyway.dev.conf** and **.flyway.it.conf**. If you don't use the flyway maven goal directly, **.flyway.dev.conf** and **.flyway.it.conf** are obsolete. 

**Do not commit your dotfiles .env and .flyway.*.conf!**
