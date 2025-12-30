FROM gradle:8.10.2-jdk21 AS builder
WORKDIR /workspace

# 의존성 캐싱을 위한 레이어 분리
COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle settings.gradle ./

# 의존성만 먼저 다운로드 (캐싱 최적화)
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon || true

# 소스 코드 복사 및 빌드
COPY src src
RUN ./gradlew clean bootJar -x test --no-daemon \
    && cp build/libs/*SNAPSHOT.jar app.jar

FROM eclipse-temurin:21-jre
WORKDIR /app

# 비 root 사용자 생성 (보안 강화)
RUN groupadd -r spring && useradd -r -g spring spring
RUN chown -R spring:spring /app
USER spring

COPY --from=builder /workspace/app.jar app.jar

EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app/app.jar"]
