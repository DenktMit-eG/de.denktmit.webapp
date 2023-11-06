# DenktMit eG - Webapp
This is the default development base for DenktMit eG Webapps. It is set up
as three-tier architecture with a persistence, a business and web layer 
for server-side HTML, as well as JSON and XML rendering

## Local development
Please not, that any paths used in this documentation are relative to this
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
COMPOSE_PROFILES=dev,it docker compose -f docker-compose.dev.yaml up -d
```

Once you are done with it, you can just stop it, so it does not use up
your system resources

```bash
COMPOSE_PROFILES=dev,it docker compose -f docker-compose.dev.yaml stop
```

and restart it, when you need it again

```bash
COMPOSE_PROFILES=dev,it docker compose -f docker-compose.dev.yaml --profile dev start
```

In case you need a fresh start, you can tell docker-compose to tear down
everything and start anew.

```bash
# Read the .env file into environment variables
ENV_FILE="$(pwd)/.env-dev"
source ${ENV_FILE}
# Tear down docker containers and remove volumes
COMPOSE_PROFILES=dev,it,e2e docker compose -f docker-compose.dev.yaml down
# Remove the persistent docker volumes
docker volume rm ${COMPOSE_PROJECT_NAME}_db-dev-data ${COMPOSE_PROJECT_NAME}_db-it-data
```

#### Build the application
Make sure, your service dependencies are running. Then you can just run
the build with

```bash
./mvnw clean install
```

#### Prepare the database with testdata
The pom.xml file of the persistence module defines Flyway executions to
fill the dev and it databases with testdata.

Fill dev database with testdata
```bash
./mvnw flyway:migrate@fill-dev -f ./persistence/pom.xml
```

Fill integration test database with testdata
```bash
./mvnw flyway:migrate@fill-it -f ./persistence/pom.xml
```

#### Start the application
Visit [http://localhost:8080](http://localhost:8080) to open the ui page

## End-to-end testing with Selenium
The simple webapp comes with full end-to-end testing features, based on
Selenium Hub and a set of browsers. 

To start Selenium hub within existing docker webapp context type

```bash
COMPOSE_PROFILES=e2e docker compose -f docker-compose.dev.yaml up -d
```

And to stop it again issue an

```bash
COMPOSE_PROFILES=e2e docker compose -f docker-compose.dev.yaml down
```