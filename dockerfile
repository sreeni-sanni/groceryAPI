FROM openjdk:8

COPY target/groceryAPI-0.0.1-SNAPSHOT.jar groceryAPI-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/groceryAPI-0.0.1-SNAPSHOT.jar"]
