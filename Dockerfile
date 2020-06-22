FROM openjdk:8-jdk-alpine

ARG JAR_FILE=target/bookstore-1.jar

RUN mkdir -p app/
RUN mkdir -p app/logs/

COPY ${JAR_FILE} app/app.jar

ENTRYPOINT ["java","-jar","app/app.jar"]