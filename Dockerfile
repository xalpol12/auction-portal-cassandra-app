FROM openjdk:23-jdk
WORKDIR /app
COPY target/bug-tracking-casandra-app.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]