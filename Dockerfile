# 1단계: 빌드
FROM gradle:7.6-jdk17 AS builder
WORKDIR /app
COPY . .
RUN ./gradlew clean build --no-daemon

# 2단계: 런타임
FROM openjdk:17-jdk-alpine
COPY --from=builder /app/build/libs/*.jar /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
