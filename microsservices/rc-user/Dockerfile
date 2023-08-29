FROM maven:3.8.2-openjdk-17 AS build

COPY src /app/src
COPY pom.xml /app

RUN mvn package -DskipTests=true

FROM openjdk:17

COPY  --from=build /app/target/rc-user.jar /app/rc-user.jar

CMD ["java", "-jar", "/app/rc-user.jar", "--spring.profiles.active=prod"]