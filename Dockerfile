FROM openjdk:8-jdk-slim
COPY "./target/ms-bank-account-0.0.1-SNAPSHOT.jar" "app.jar"
EXPOSE 8002
ENTRYPOINT ["java", "-jar", "app.jar"]