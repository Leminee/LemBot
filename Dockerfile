FROM openjdk:latest
ADD target/lembotv1.jar lembotv1.jar
ADD example.config.yml example.config.yml
ENTRYPOINT ["java", "-jar","/lembotv1.jar"]