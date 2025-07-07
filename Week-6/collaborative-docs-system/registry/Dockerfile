FROM eclipse-temurin:17-jre
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY 4{JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
