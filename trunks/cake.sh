#!/bin/bash

if [ "$1" = "javadoc" ]; then
   mvn javadoc:javadoc
  exit
elif [ "$1" = "clover" ]; then
   mvn -Dcoverage clover2:instrument clover2:aggregate clover2:clover
  exit
elif [ "$1" = "javadoc-deploy" ]; then
   mvn clean javadoc:javadoc
   mvn -N site:deploy
   exit
elif [ "$1" = "javadocx-deploy" ]; then
   mvn clean javadoc:javadoc
   mv target/site/apidocs target/site/apidocsx
   mvn -N site:deploy
   exit
elif [ "$1" = "site" ]; then  
   mvn -f cake-distribution/site/pom.xml clean install
   echo site available in  cake-distribution/site/target/site
   exit
elif [ "$1" = "site-deploy" ]; then  
   mvn -f cake-distribution/site/pom.xml clean install site:deploy
   exit
elif [ -z "$1" ]; then 
	echo Usage: $0 target
	echo where target is:
else
	echo Unknown target: "$1"
	echo Valid targets are:

fi

echo "  clover           Runs and Builds clover reports"
echo "  javadoc          Builds javadoc"    
echo "  javadoc-deploy   Builds javadoc and deploys to http://cake.codehaus.org/apidocs"
echo "  javadocx-deploy  Builds javadoc and deploys to http://cake.codehaus.org/apidocsx"
echo "  site             Builds site"
echo "  site-deploy      Builds site and deploys to http://cake.codehaus.org"



