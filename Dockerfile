# Stage 1: Maven Build Stage
FROM maven:3.8.5-openjdk-17 AS builder

WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Runtime Stage
FROM openjdk:17-jdk-slim

WORKDIR /app
EXPOSE 2032

# Set environment variables for database connection
ENV DB_HOST=your-database-host \
    DB_PORT=3306 \
    DB_NAME=your-database-name \
    DB_USER=your-username \
    DB_PASSWORD=your-password

# Copy the built JAR file
COPY --from=builder /app/target/Farmerproduct-0.0.1-SNAPSHOT.jar app.jar

# Run the application
CMD ["java", "-jar", "app.jar"]
