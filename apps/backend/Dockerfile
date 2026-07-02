# STAGE 1: Build
FROM eclipse-temurin:25-jdk-noble AS builder

WORKDIR /app

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
COPY libs/ libs/

RUN --mount=type=cache,target=/root/.m2 \
    ./mvnw install:install-file \
    -Dfile=libs/auth-core-spring-boot-starter-1.0.0.jar \
    -DpomFile=libs/auth-core-spring-boot-starter-1.0.0.pom -B -q

RUN --mount=type=cache,target=/root/.m2 \
    ./mvnw dependency:go-offline -B -q

COPY src/ src/

RUN --mount=type=cache,target=/root/.m2 \
    ./mvnw package -B -q -DskipTests

# STAGE 2: Runtime
FROM eclipse-temurin:25-jre-noble AS runtime

WORKDIR /app

RUN groupadd --system appgroup && \
    useradd --system --gid appgroup --no-create-home appuser

COPY --from=builder /app/target/*.jar app.jar

RUN chown appuser:appgroup app.jar

USER appuser
EXPOSE 8080

ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-jar", "app.jar"]
