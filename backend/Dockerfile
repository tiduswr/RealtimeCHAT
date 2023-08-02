FROM maven:3.8.2-openjdk-17 AS build

COPY src /app/src
COPY pom.xml /app

WORKDIR /app

RUN mvn package -DskipTests=true

FROM openjdk:17

COPY --from=build /app/target/app.jar /app/app.jar

CMD ["java", "-jar", "/app/app.jar", "--spring.profiles.active=prod"]