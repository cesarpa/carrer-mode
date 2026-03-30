# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copy only pom.xml for dependency resolution to cache layers
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source and build
COPY src ./src
RUN mvn package -DskipTests

# Stage 2: Run
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Create data directory and copy existing database files
RUN mkdir ./data && chmod 777 ./data
COPY data/ ./data/

# Copy the JAR from the build stage
COPY --from=build /app/target/carrer-mode-0.0.1-SNAPSHOT.jar app.jar

# Render sets the PORT environment variable
ENV PORT=8080
EXPOSE ${PORT}

# Run the application
ENTRYPOINT java -Dserver.port=${PORT:-8080} -jar app.jar
