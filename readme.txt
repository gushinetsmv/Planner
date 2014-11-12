Short instructions describe steps required to build & setup & deploy Planner application to Tomcat servlet container.

Pre-requirements:
1. Tomcat is installed and configured, version >= 6.0
2. Postgres database is installed and configured, version >= 9.1.3
3. Ant or Maven build tool is installed and configured.

Ant instruction:
----------------------------------------
0. create database and fill it with planner.dump using ANT command:
   ant restoreDB
1. build application with simple ant command : 
   ant war 
2. copy build/planner.war file to ${TOMCAT_HOME}/webapps
3. start tomcat and open in browser the url below: 
         http://127.0.0.1:8080/planner - in case of local default tomcat installation
    or   http://$tomcatHost:$tomcatPort/planner - in case of remote or specific tomcat settings


Maven instruction:
----------------------------------------
0. create database using any postgres console and fill it with planner.dump. 
   Commands are:
   createdb --username=your_username -T template0 new_db_name
   psql --username=your_username new_db_name< planner.dump
1. build application with simple maven command : 
   mvn clean install 
2. copy target/planner.war file to ${TOMCAT_HOME}/webapps
3. start tomcat and open in browser the url below: 
         http://127.0.0.1:8080/planner - in case of local default tomcat installation
    or   http://$tomcatHost:$tomcatPort/planner - in case of remote or specific tomcat settings
