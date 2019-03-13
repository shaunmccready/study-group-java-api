# Study Group Meetings Backend Java Api

Study group meeting organizer - Backend API

* _ReactJs user interface repository is located at (https://github.com/pachoclo/study-group-react-ui)_

* _PostgreSQL database_repository is located at (https://github.com/shaunmccready/study-group-postgres-db)_


***
*** 

### Tech stack used:
- [Spring Boot 2.1.3](https://spring.io/projects/spring-boot)
- [Java v1.8](https://www.oracle.com/technetwork/java/javase/overview/java8-2100321.html)
- [Auth0 service for authentication](https://auth0.com/)
- [PostgreSQL v11 for persistence](https://www.postgresql.org/docs/11/index.html)
- [Docker CE](https://www.docker.com/why-docker)



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

Build project in command line using: `mvn clean install`

To run the project from the project's root: `mvn spring-boot:run`
or `java -jar ./target/study-group-meetings-api.jar`

