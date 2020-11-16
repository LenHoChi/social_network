FROM adoptopenjdk/openjdk11:alpine-jre
ADD target/hochilen.jar hochilen.jar
ENTRYPOINT ["java","-jar","hochilen.jar"]