FROM openjdk:17-jdk-alpine
VOLUME /tmp
COPY target/employe-service-0.0.1-SNAPSHOT.jar employe-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","employe-service-0.0.1-SNAPSHOT.jar"]
EXPOSE 8081