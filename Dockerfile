FROM openjdk:8-jdk-alpine
LABEL maintainer="praveen2099636@gmail.com"
EXPOSE 8000
ADD target/tweet-app.jar tweet-app.jar
ENTRYPOINT ["java","-jar","/tweet-app.jar"]