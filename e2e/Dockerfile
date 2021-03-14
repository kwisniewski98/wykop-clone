FROM maven:3.6.3-openjdk-17 as build

COPY . /build/.

COPY e2e/application.properties /build/src/main/resources/

COPY e2e/sql/. /build/src/main/resources/

COPY e2e/pom.xml /build/

WORKDIR /build

RUN mvn package

ENTRYPOINT [ "java", "-jar", "target/vykop.jar"]