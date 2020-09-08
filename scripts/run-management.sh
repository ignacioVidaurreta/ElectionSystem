#!/bin/bash
if [ "$#" -ne 2 ]; then
    echo "Invalid Arguments. Usage: \`./run-management.sh -Daction=[open|close|status] -DserverAddress=<ip>\`"
    exit 1;
fi

cd ../client/target/election-system-client-1.0-SNAPSHOT/ || (echo "Unexpected error" && exit 1)
./run-management.sh "$@"
