FROM gradle:8.10.2-jdk21 AS builder
WORKDIR /workspace

COPY . .
RUN ./gradlew clean bootJar -x test --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=builder /workspace/build/libs/*SNAPSHOT.jar app.jar

ENV JAVA_OPTS="-Xms512m -Xmx768m"

EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]