FROM adoptopenjdk:17-jdk-hotspot
EXPOSE 8080
ADD target/bloomberg-docker.jar bloomberg-docker
ENTRYPOINT ["java", "-jar", "bloomberg-docker.jar"]
