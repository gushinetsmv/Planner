Short instructions describes steps required to build & setup & deploy Planner application to Tomcat servlet container.

Pre-requirements:
1. Tomcat is installed and configured, version >= 6.0
2. Postgres database is installed and configured, version >= 9.1.3
3. Ant or Maven build tool is installed or configured.

Ant instruction:
----------------------------------------


Maven instruction:
----------------------------------------
0. create database using any postgres console.  (TODO!: Опиши команду) 
1. build application with simple maven command : 
   mvn clean install 
2. copy target/planner.war file to ${TOMCAT_HOME}/webapps
3. start tomcat and open in browser the url below: 
         http://127.0.0.1:8080/planner - in case of local default tomcat installation
    or   http://$tomcatHost:$tomcatPort/planner - in case of remote or specific tomcat settings
