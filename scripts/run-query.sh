#!/bin/bash

if [ $# -ne 2 ]; then
    echo "Invalid arguments; Usage: ./run-vote -DserverAddress=<ip> -DoutPath=<filename> [ -Dstate=<stateName> | -Did=<pollingPlaceNumber> ]"
    exit 1
fi

cd ../client/target/election-system-client-1.0-SNAPSHOT/ || (echo "Error!"; exit 1)
./run-query.sh "$@"