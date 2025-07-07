FROM eclipse-temurin:17-jre
VOLUME /tmp
ARG JAR_FILE=config/target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"] 