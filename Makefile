# Master build for eclipsem2 and maven-repo-server

ALL : server plugin

clean-server:
	cd maven-repo-server && mvn clean

clean-plugin:
	cd com.coconut_palm_software.eclipsem2.releng && mvn clean

clean: clean-server clean-plugin

plugin:
	cd com.coconut_palm_software.eclipsem2.releng && mvn clean package

server:
	cd maven-repo-server && mvn clean package

