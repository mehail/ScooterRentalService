FROM adoptopenjdk:11-jre-hotspot
ARG JAR_FILE=*.jar
ARG WAR_FILE=*.war
COPY ${JAR_FILE} core-0.0.1-SNAPSHOT.jar
COPY ${WAR_FILE} web-0.0.1-SNAPSHOT.war
ENTRYPOINT ["java", "-jar", "web-0.0.1-SNAPSHOT.war"]