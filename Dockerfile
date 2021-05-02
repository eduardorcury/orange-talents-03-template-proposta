FROM maven:3.6.3-jdk-11 AS builder
WORKDIR /app
COPY src src
COPY pom.xml .
RUN mvn -f pom.xml clean package

FROM openjdk:11
COPY --from=builder /app/target/Proposta-0.0.1-SNAPSHOT.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]