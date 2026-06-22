FROM openjdk:22-jdk

LABEL authors="juned"

COPY target/royal_spa.jar /royal_spa.jar

ENTRYPOINT ["java", "-jar", "/royal_spa.jar"]