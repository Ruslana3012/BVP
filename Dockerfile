FROM openjdk:17-jdk

COPY target/Junior-Java-Developer-BVP-Software.jar .
COPY /db/migrations /Junior-Java-Developer-BVP-Software/db/migrations

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "Junior-Java-Developer-BVP-Software.jar"]

FROM postgres:16

ENV POSTGRES_DB postgres
ENV POSTGRES_USER postgres
ENV POSTGRES_PASSWORD vetabe