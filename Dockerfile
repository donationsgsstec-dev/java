# Multi-stage build for Spring Boot application
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn
COPY pom.xml .

# Make mvnw executable
RUN chmod +x ./mvnw

# Download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-jammy

# Set working directory
WORKDIR /app

# Copy the built artifact from build stage
COPY --from=build /app/target/user-auth-app-1.0.0.war app.war

# Expose port (Render will override with $PORT)
EXPOSE 8080

# Set JVM options for memory management
ENV JAVA_TOOL_OPTIONS="-Xmx512m -Xms256m"

# Run the application
ENTRYPOINT ["java", "-Dserver.port=${PORT:-8080}", "-jar", "app.war"]

# Made with Bob
