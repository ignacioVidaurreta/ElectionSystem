#!/bin/bash

if [ $# -ne 2 ]; then
    echo "Invalid arguments; Usage: ./run-management -Daction=[open|close|status] -DserverAddress=<ip>"
fi

cd ../client/target/election-system-client-1.0-SNAPSHOT/ || (echo "Error!"; exit 1)
./run-management.sh "$@"