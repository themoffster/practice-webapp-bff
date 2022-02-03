FROM openjdk:11
ARG JAR_FILE=/build/libs/practice-webapp-bff-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} practice-webapp-bff.jar
CMD ["java", "-jar", "-Dspring.profiles.active=local", "/practice-webapp-bff.jar"]
