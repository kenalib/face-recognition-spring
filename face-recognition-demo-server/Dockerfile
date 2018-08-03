# https://stackoverflow.com/questions/40636338
FROM centos:7
RUN yum install -y \
       java-1.8.0-openjdk \
       java-1.8.0-openjdk-devel
# ENV JAVA_HOME /etc/alternatives/jre
# https://spring.io/guides/gs/spring-boot-docker/
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
