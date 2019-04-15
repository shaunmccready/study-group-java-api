FROM openjdk:11.0.2-jdk-slim-stretch
MAINTAINER Shaun McCready
EXPOSE 8080
ARG JAR_FILE=target/study-group-java-api.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
