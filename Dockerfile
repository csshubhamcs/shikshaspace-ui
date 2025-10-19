# Multi-stage build - Build stage
FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /app

# Copy Gradle wrapper and config
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

# Copy source code
COPY src ./src

# Build with Vaadin production mode
RUN chmod +x gradlew && \
    ./gradlew build -Pvaadin.productionMode -x test --no-daemon

# Runtime stage - Optimized for production
FROM eclipse-temurin:17-jre-alpine

# Install curl for health checks
RUN apk add --no-cache curl

WORKDIR /app

# Copy JAR from build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose port
EXPOSE 7500

# JVM optimization for containerized environment
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
