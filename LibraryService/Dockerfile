# Use a Maven image to build the project
FROM maven:3.8.6-eclipse-temurin-17 AS build
WORKDIR /build

# Copy the pom.xml and download dependencies to the cache layer
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the rest of the source code and build the project
COPY src ./src
RUN mvn clean package -DskipTests

# Use a slim OpenJDK image for the runtime
FROM openjdk:17-jdk-alpine
RUN apk update && apk add curl
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /build/target/*.jar app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
