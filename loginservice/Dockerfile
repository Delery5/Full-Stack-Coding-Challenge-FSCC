FROM openjdk:17-jdk-alpine
VOLUME /tmp
COPY target/loginservice-0.0.1-SNAPSHOT.jar loginservice-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","loginservice-0.0.1-SNAPSHOT.jar"]
EXPOSE 8083