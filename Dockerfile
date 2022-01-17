FROM openjdk:latest
ADD target/lembotv3.jar lembotv3.jar
ADD example.config.yml example.config.yml
ENTRYPOINT ["java", "-jar","/lembotv3.jar"]