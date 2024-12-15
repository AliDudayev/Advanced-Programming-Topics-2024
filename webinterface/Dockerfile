FROM openjdk:17-jdk-alpine
EXPOSE 8086
ADD target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]