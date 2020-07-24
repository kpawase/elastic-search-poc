FROM openjdk:8-jdk-alpine as build

RUN apk add --no-cache curl tar bash

ARG MAVEN_VERSION=3.3.9

ARG USER_HOME_DIR="/root"

RUN mkdir -p /usr/share/maven && \

curl -fsSL http://apache.osuosl.org/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz | tar -xzC /usr/share/maven --strip-components=1 && \

ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

ENV MAVEN_HOME /usr/share/maven

ENV MAVEN_CONFIG "$USER_HOME_DIR/.m2"

ENV MAVEN_OPTS="-XX:+TieredCompilation -XX:TieredStopAtLevel=1"

ENTRYPOINT ["/usr/bin/mvn"]

RUN mkdir -p /usr/src/app

WORKDIR /usr/src/app

# install maven dependency packages (keep in image)

COPY pom.xml /usr/src/app

COPY src /usr/src/app/src

RUN mvn -T 1C clean install


FROM openjdk:8


COPY --from=build /usr/src/app/target/elasticsearch-demo.jar ./elasticsearch-demo.jar

EXPOSE 8081

ENV elasticsearch.host=elasticsearch

ENV index.name=transaction_ind

ENTRYPOINT ["java","-Delasticsearch.host=elasticsearch -Dindex.name=transaction_ind","-jar", "./elasticsearch-demo.jar"]








