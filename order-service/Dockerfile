# Order Service Dockerfile
FROM eclipse-temurin:21-jre-alpine

LABEL maintainer="actora-team"
LABEL application="order-service"
LABEL version="1.0.0"

RUN addgroup -S spring && adduser -S spring -G spring

WORKDIR /app

COPY target/*.jar app.jar

RUN chown -R spring:spring /app

USER spring

EXPOSE 8082

HEALTHCHECK --interval=30s --timeout=10s --retries=3 --start-period=60s \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8082/actuator/health || exit 1

ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]
