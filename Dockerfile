FROM openjdk:17-jdk-slim-buster
WORKDIR /app

COPY /build/libs/wallet_service-0.0.1-SNAPSHOT.jar build/

WORKDIR /app/build
EXPOSE 8080
ENTRYPOINT java -jar wallet_service-0.0.1-SNAPSHOT.jar
