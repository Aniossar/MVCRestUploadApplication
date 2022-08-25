FROM openjdk:17
ADD /target/CalculatorMVCServer.jar backend.jar
ENTRYPOINT ["java", "-jar", "backend.jar"]