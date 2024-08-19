FROM openjdk:17-jdk

#VOLUME /tmp

COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "-Duser.timezone=Asia/Bishkek", "/app.jar"]