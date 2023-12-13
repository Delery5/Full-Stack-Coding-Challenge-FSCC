FROM openjdk:17-jdk-alpine
VOLUME /tmp
COPY target/registrationservice-0.0.1-SNAPSHOT.jar registrationservice-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","registrationservice-0.0.1-SNAPSHOT.jar"]
EXPOSE 8082