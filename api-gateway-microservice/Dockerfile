FROM openjdk:8-alpine

ARG JAR_FILE=run/target/*.jar

RUN mkdir "/opt/eureka-client-api-gateway-service"

COPY ${JAR_FILE}  /opt/eureka-client-api-gateway-service/app.jar

ENTRYPOINT ["java", "-jar", "/opt/eureka-client-api-gateway-service/app.jar"]