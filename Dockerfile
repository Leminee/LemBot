FROM openjdk:17
ADD target/lembotv3.jar lembotv3.jar
ENTRYPOINT ["java", "-jar","/lembotv3.jar"]