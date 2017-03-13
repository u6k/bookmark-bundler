FROM openjdk:8-alpine
MAINTAINER u6k.apps@gmail.com

RUN mkdir -p /opt
WORKDIR /opt
COPY target/bookmark-bundler.jar .

VOLUME /var/lib/bookmark/hsqldb

EXPOSE 8080

CMD ["java", "-jar", "/opt/bookmark-bundler.jar"]
