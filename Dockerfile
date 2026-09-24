#
# Build stage
#
FROM maven:3.9-eclipse-temurin-21 AS build
ENV HOME=/usr/app
RUN mkdir -p $HOME
WORKDIR $HOME
ADD ./src $HOME/src
ADD ./pom.xml $HOME/pom.xml
RUN mvn clean package -Dmaven.test.skip=true

#
# Package stage
#
FROM eclipse-temurin:21-jdk
ENV MYSQL_HOST=db
ENV MYSQL_PORT=3306
ENV MYSQL_DATABASE=slimstore_prod
ENV MYSQL_USERNAME=root
ENV MYSQL_PASSWORD=1234
ENV SQL_TO_CONSOLE=false
ENV HTTP_PORT=8000
ENV REDIS_HOST=redis
ENV REDIS_NAMESPACE=slimstore_prod
ENV ADMIN_PASSWORD=4321
ARG JAR_FILE=/usr/app/target/*.jar
COPY --from=build $JAR_FILE /app/runner.jar
EXPOSE $HTTP_PORT
ENTRYPOINT java -jar /app/runner.jar