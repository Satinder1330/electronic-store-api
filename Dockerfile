FROM eclipse-temurin:21-jdk

ARG JAR_FILE=jar-files/*.jar

#Copy jar into container
COPY ${JAR_FILE} app.jar
EXPOSE 8080
#Run the jar file
ENTRYPOINT ["java", "-jar", "/app.jar"]
