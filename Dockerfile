# Multi-stage build for Spring Boot application
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /app

# Copy pom.xml first for dependency caching
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

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

# Run the application using shell form to allow environment variable expansion
CMD java -Dserver.port=${PORT:-8080} -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE:-production} -jar app.war

# Made with Bob
