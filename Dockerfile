FROM openjdk:latest
ADD lembotv1.jar lembotv1.jar
ADD lembot.config.yml lembot.config.yml
RUN mkdir /attachments
ENTRYPOINT ["java", "-jar","/lembotv1.jar"]