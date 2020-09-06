#!/bin/bash

java -classpath ../../../../target/classes:../../../../../api/target/classes:"$HOME"/.m2/repository/org/slf4j/slf4j-api/1.7.12/slf4j-api-1.7.12.jar:"$HOME"/.m2/repository/org/slf4j/slf4j-log4j12/1.7.12/slf4j-log4j12-1.7.12.jar:"$HOME"/.m2/repository/log4j/log4j/1.2.17/log4j-1.2.17.jar:"$HOME"/.m2/repository/org/slf4j/jcl-over-slf4j/1.7.12/jcl-over-slf4j-1.7.12.jar "$@" "ar.edu.itba.pod.g3.client.VotingClient"

