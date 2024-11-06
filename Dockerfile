#
# Build stage
#
FROM eclipse-temurin:17-jdk-jammy AS build
ENV HOME=/usr/app
RUN mkdir -p $HOME
WORKDIR $HOME
ADD ./src $HOME/src
ADD ./pom.xml $HOME/pom.xml
ADD ./.mvn $HOME/.mvn
ADD ./mvnw $HOME/mvnw
RUN ./mvnw clean package -Dmaven.test.skip=true

#
# Package stage
#
FROM eclipse-temurin:17-jre-jammy 
ENV MYSQL_HOST=db
ENV MYSQL_PORT=3306
ENV MYSQL_DATABASE=slimstore_prod
ENV MYSQL_USERNAME=root
ENV MYSQL_PASSWORD=1234
ENV HTTP_PORT=8080
ARG JAR_FILE=/usr/app/target/*.jar
COPY --from=build $JAR_FILE /app/runner.jar
EXPOSE 8080
ENTRYPOINT java -jar /app/runner.jar