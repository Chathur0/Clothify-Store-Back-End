FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/target/clothify-project-back-end-1.0-SNAPSHOT.jar .
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/clothify-project-back-end-1.0-SNAPSHOT.jar"]