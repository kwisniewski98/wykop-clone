FROM maven:3.6.3-openjdk-17 as build

COPY . /build/.

WORKDIR /build

RUN mvn package

FROM openjdk:17-jdk-alpine

COPY --from=build /build/target/vykop.jar /.

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/vykop.jar"]