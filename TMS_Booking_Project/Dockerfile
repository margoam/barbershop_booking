# Build stage
FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /TMS_Booking_Project
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM openjdk:21-jdk
WORKDIR /TMS_Booking_Project
COPY --from=build /TMS_Booking_Project/target/TMS_Booking_Project-0.1-release.jar .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "TMS_Booking_Project-0.1-release.jar"]
