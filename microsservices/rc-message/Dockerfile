FROM maven:3.8.2-openjdk-17 AS build

COPY src /app/src
COPY pom.xml /app

RUN mvn package -DskipTests=true

FROM openjdk:17

COPY  --from=build /app/target/rc-message.jar /app/rc-message.jar

CMD ["java", "-jar", "/app/rc-message.jar", "--spring.profiles.active=prod"]