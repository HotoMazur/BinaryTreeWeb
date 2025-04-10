FROM gradle:8.13.0-jdk21-alpine AS build
LABEL authors="dmazurev"
WORKDIR /app
COPY build.gradle .
COPY settings.gradle .
COPY src ./src
RUN gradle clean build -x test

FROM eclipse-temurin:23-jre-ubi9-minimal
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
CMD ["java", "-jar", "app.jar"]