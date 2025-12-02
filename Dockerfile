FROM gradle:8.10.2-jdk21 AS builder
WORKDIR /workspace

COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle settings.gradle ./
COPY src src

RUN chmod +x gradlew \
    && ./gradlew clean bootJar -x test \
    && cp build/libs/*SNAPSHOT.jar app.jar

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=builder /workspace/app.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
