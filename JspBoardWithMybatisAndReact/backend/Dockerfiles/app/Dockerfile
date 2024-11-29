FROM gradle:jdk21 AS builder

WORKDIR /build

COPY . /build

RUN gradle build --exclude-task test

FROM openjdk:21-slim

WORKDIR /app

COPY --from=builder /build/build/libs/*-SNAPSHOT.jar app.jar

EXPOSE 8080

USER nobody

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]