#!/bin/bash

if [ "$#" -ne 3 ]; then
   echo "Error, usage: ./run-fiscal.sh -DserverAddress=<ip> -Did=<booth> -Dparty=<PartyName>";
   exit 1;
fi

cd ../client/target/election-system-client-1.0-SNAPSHOT/ || (echo "Error!"; exit 1)
./run-fiscal.sh "$@"