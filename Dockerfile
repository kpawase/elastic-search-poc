FROM openjdk:8

MAINTAINER kppawase@gmail.com

ADD target/elasticsearch-demo.jar elasticsearch-demo.jar

EXPOSE 8081

ENV elasticsearch.host=elasticsearch

ENTRYPOINT ["java","-Delasticsearch.host=elasticsearch","-jar","elasticsearch-demo.jar"]


