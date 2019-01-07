FROM tomcat:7-jre7

ARG source=./gitlab/
ARG contentStore=/home/tomcat7/content/gitlab/
ARG contextName=ROOT

COPY $source $contentStore

WORKDIR /usr/local/tomcat/

COPY ./docker/context.xml ./conf
RUN  rm -rf webapps/*

COPY ./iris/target/iris.war ./webapps
RUN mv ./webapps/iris.war ./webapps/$contextName.war

ENTRYPOINT ["catalina.sh", "run"]