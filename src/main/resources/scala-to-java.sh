#!/bin/bash

#args
#path of files to translate
path=/.../module/root
#repository of  jars that the path is dependent on
repo=/.../.m2/repository


echo "running java -jar scala-to-java.jar"
echo "path:" $path
echo "repo:" $repo

#run
java -jar -Dpath=path -Drepo=$repo scala-to-java.jar --slim

echo "done"
