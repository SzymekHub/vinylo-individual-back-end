# I use the official Gradle image with version 8.10.1 and JDK 17 installed
FROM gradle:8.10.1-jdk17
# I Set the working directory inside the container to /opt/app
WORKDIR /opt/app

# I copied the JAR file from the build/libs folder on the my laptop (host machine) 
# to the container's working directory
# And I Renamed it to Vinylo.jar for simplicity when running the application
COPY build/libs/Vinylo-Individual-0.0.1-SNAPSHOT.jar Vinylo.jar

# I defined a build argument for Java options that can be passed during the build process
ARG JAVA_OPTS

# And I set the environment variable JAVA_OPTS with the value of the build argument
ENV JAVA_OPTS=$JAVA_OPTS

# Defined the entrypoint for the Docker container
# This can be used to run the JAR with additional options via JAVA_OPTS
# These options can include settings like memory limits, garbage collection strategies, or debugging configurations. 
#ENTRYPOINT exec java $JAVA_OPTS -jar Vinylo.jar

# For Spring-Boot project, I can use the entrypoint below to reduce Tomcat startup time.
# The `-Djava.security.egd=file:/dev/./urandom` flag helps to reduce the time it takes for Tomcat to initialize 
# by using a faster source of entropy (urandom) for secure random number generation
ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar Vinylo.jar