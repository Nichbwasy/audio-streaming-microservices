FROM openjdk:8-alpine

ARG JAR_FILE=run/target/*.jar

RUN mkdir "/opt/eureka-client-authorization-service"

COPY ${JAR_FILE}  /opt/eureka-client-authorization-service/app.jar

ENTRYPOINT ["java", "-jar", "/opt/eureka-client-authorization-service/app.jar"]