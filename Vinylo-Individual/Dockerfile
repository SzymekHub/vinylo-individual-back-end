FROM gradle:8.10.1-jdk17
WORKDIR /opt/app
COPY build/libs/Vinylo-Individual-0.0.1-SNAPSHOT.jar Vinylo.jar

ARG JAVA_OPTS
ENV JAVA_OPTS=$JAVA_OPTS
#ENTRYPOINT exec java $JAVA_OPTS -jar Vinylo.jar
# For Spring-Boot project, use the entrypoint below to reduce Tomcat startup time.
ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar Vinylo.jar