FROM openjdk:8
EXPOSE 8000
ADD target/tweet-app.jar tweet-app.jar
ENTRYPOINT ["java","-jar","tweet-app.jar"]
