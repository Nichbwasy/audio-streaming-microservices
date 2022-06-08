FROM openjdk:8-alpine

ARG JAR_FILE=run/target/*.jar

RUN mkdir "/opt/eureka-server"

COPY ${JAR_FILE}  /opt/eureka-server/app.jar

ENTRYPOINT ["java", "-jar", "/opt/eureka-server/app.jar"]