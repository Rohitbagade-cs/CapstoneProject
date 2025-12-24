# Step 1: Use Maven to build app
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# Step 2: Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Step 3: Copy source code
COPY src ./src

# Step 4: Build jar
RUN mvn clean package -DskipTests

# Step 5: Lightweight Java runtime
FROM eclipse-temurin:17-jre
WORKDIR /app

# Step 6: Copy jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Step 7: Expose backend port
EXPOSE 8080

# Step 8: Run app
ENTRYPOINT ["java","-jar","app.jar"]
