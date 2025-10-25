FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./
COPY src ./src
COPY frontend ./frontend

RUN chmod +x gradlew && ./gradlew vaadinBuildFrontend build -x test --no-daemon

FROM eclipse-temurin:21-jre-alpine
RUN apk add --no-cache curl

WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
COPY --from=build /app/build/vaadin-generated /app/vaadin-generated

EXPOSE 7500

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-Dvaadin.productionMode=true", "-jar", "app.jar"]
