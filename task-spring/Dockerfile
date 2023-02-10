FROM openjdk:17-alpine
ADD ./target/task-spring-0.0.1-SNAPSHOT.jar backend.jar
ENTRYPOINT ["java", "-jar", "backend.jar"]