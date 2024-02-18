FROM openjdk:17-jdk-alpine
COPY target/tesla-1.3.0.0.jar java-backend-tesla.jar
ENTRYPOINT ["java", "-jar", "java-backend-tesla.jar"]