FROM adoptopenjdk/openjdk11:alpine-jre
RUN mkdir -p /usr/app
WORKDIR /usr/app
COPY ${JAR_FILE} /CalculatorMVCServer.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","CalculatorMVCServer.jar"]