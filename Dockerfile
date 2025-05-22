FROM openjdk:17-jdk-alpine

# JAR 파일을 컨테이너로 복사
ARG JAR_FILE=./build/libs/*.jar
COPY ${JAR_FILE} /app.jar

# 애플리케이션 실행 명령어
ENTRYPOINT ["java", "-jar", "/app.jar"]