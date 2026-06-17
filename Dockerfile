# Stage 1: Build the application
FROM gradle:8.6.0-jdk21 AS build
WORKDIR /app
COPY . .
# Run gradle build, excluding tests to speed up deployment
RUN chmod +x ./gradlew
RUN ./gradlew clean build -x test

# Stage 2: Create the minimal runtime image
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# Copy the built JAR from the previous stage
COPY --from=build /app/build/libs/*.jar app.jar
# Expose port (Render defaults to 10000 or reads from PORT env)
EXPOSE 8080
# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
