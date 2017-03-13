FROM openjdk:8-alpine
MAINTAINER u6k.apps@gmail.com

WORKDIR /opt

COPY target/bookmark-bundler.jar .

EXPOSE 8080

CMD ["java", "-jar", "/opt/bookmark-bundler.jar"]
