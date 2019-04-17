# Study Group Meetings Backend Java Api

[![Build Status](https://travis-ci.org/shaunmccready/study-group-java-api.svg?branch=master)](https://travis-ci.org/shaunmccready/study-group-java-api)
[![codecov](https://codecov.io/gh/shaunmccready/study-group-java-api/branch/master/graph/badge.svg)](https://codecov.io/gh/shaunmccready/study-group-java-api)

Study group meeting organizer - Backend API

* _ReactJs user interface repository is located at (https://github.com/pachoclo/study-group-react-ui)_

* _PostgreSQL database_repository is located at (https://github.com/shaunmccready/study-group-postgres-db)_


*** 
*** 

### Tech stack used:
- [Spring Boot 2.1.3](https://spring.io/projects/spring-boot)
- [Java v11](https://www.oracle.com/technetwork/java/javase/overview/index.html)
- [Auth0: Service for authentication](https://auth0.com/)
- [PostgreSQL v11 for persistence](https://www.postgresql.org/docs/11/index.html)
- [MapStruct](http://mapstruct.org/)
- [Docker CE](https://www.docker.com/why-docker)
- [Test Containers](https://www.testcontainers.org/)
- [JUnit 5](https://junit.org/junit5/)
- [Mockito](https://site.mockito.org/)



### Purpose for the App

For Students to create and manage study groups for classes.
These groups can then schedule meetings, and each group member can indicate whether they are attending the meeting or not.


## Installation

- Context path /api/
- Running on port 8080

You will need to define the following environment variables:

- STUDYGROUPMEETINGS_API_CLIENT_ID
- STUDYGROUPMEETINGS_API_CLIENT_SECRET
- STUDYGROUPMEETINGS_API_DOMAIN
- STUDYGROUPMEETINGS_API_AUTH0_AUDIENCE
- STUDYGROUPMEETINGS_API_AUTH0_ISSUER
- STUDYGROUPMEETINGS_DB_POSTGRES_USER
- STUDYGROUPMEETINGS_DB_POSTGRES_PASSWORD
- STUDYGROUPMEETINGS_DB_NAME
- STUDYGROUPMEETINGS_DB_POSTGRES_PORT
- STUDYGROUPMEETINGS_DB_POSTGRES_HOST

Build project as a jar in command line using: `mvn clean install`

To run the project from the project's root: `mvn spring-boot:run`
or `java -jar ./target/study-group-meetings-api.jar`

## Run via Docker
1) First build the image using the Dockerfile, for ex:  `docker build -t study-group-java-api .`

2) Then heres a docker run example:
 
 `docker run --name study-group-java-api --rm -p 8080:8080 -e "STUDYGROUPMEETINGS_API_CLIENT_ID=your_auth0_client_id" -e "STUDYGROUPMEETINGS_API_CLIENT_SECRET=your_auth0_client_secret" -e "STUDYGROUPMEETINGS_API_DOMAIN=your_auth0_domain" -e "STUDYGROUPMEETINGS_API_AUTH0_AUDIENCE=your_auth0_audience" -e "STUDYGROUPMEETINGS_API_AUTH0_ISSUER=your_auth0_issuer" -e "STUDYGROUPMEETINGS_DB_POSTGRES_USER=you_postgres_user" -e "STUDYGROUPMEETINGS_DB_POSTGRES_PASSWORD=your_pg_password" -e "STUDYGROUPMEETINGS_DB_NAME=your_pg_db_name" -e "STUDYGROUPMEETINGS_DB_POSTGRES_HOST=0.0.0.0" -e "STUDYGROUPMEETINGS_DB_POSTGRES_PORT=5432" -v "your_hosts_directory":/tmp study-group-java-api`

