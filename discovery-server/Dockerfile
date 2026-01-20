# Discovery Server Dockerfile
FROM eclipse-temurin:21-jre-alpine

# Add labels for better image management
LABEL maintainer="actora-team"
LABEL application="discovery-server"
LABEL version="1.0.0"

# Create non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring

# Set working directory
WORKDIR /app

# Copy the jar file
COPY target/*.jar app.jar

# Set ownership
RUN chown -R spring:spring /app

# Switch to non-root user
USER spring

# Expose port
EXPOSE 8761

# Health check
HEALTHCHECK --interval=30s --timeout=10s --retries=3 --start-period=40s \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8761/actuator/health || exit 1

# JVM optimizations for containers
ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]
