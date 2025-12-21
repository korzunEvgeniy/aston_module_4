FROM openjdk:21-jdk-slim
WORKDIR /app
EXPOSE 8888
COPY target/user-service-*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]