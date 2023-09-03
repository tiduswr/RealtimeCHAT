FROM maven:3.8.2-openjdk-17 AS build

WORKDIR /app

COPY src /app/src
COPY pom.xml /app

RUN mvn package -DskipTests=true

FROM openjdk:17

COPY  --from=build /app/target/rc-email-worker.jar /app/rc-email-worker.jar

CMD ["java", "-jar", "/app/rc-email-worker.jar", "--spring.profiles.active=prod"]