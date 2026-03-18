FROM tomcat:10.1-jdk21

RUN rm -rf /usr/local/tomcat/webapps/*

COPY build/taskapp.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
