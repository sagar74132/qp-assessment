# Build stage
FROM maven:3.8.4-openjdk-17 AS build

WORKDIR /app

# Copy the entire project
COPY . .

# Cache maven dependecies
RUN mvn dependency:go-offline

# Run maven build
RUN mvn clean install -DskipTests

# Runtime stage
FROM amazoncorretto:17-alpine

WORKDIR /app

# Copy the jar from build stage
COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
