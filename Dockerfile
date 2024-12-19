FROM openjdk:23
WORKDIR /app
COPY target/llm-middleware-0.0.1-SNAPSHOT.jar llm-middleware-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "llm-middleware-0.0.1-SNAPSHOT.jar"]
