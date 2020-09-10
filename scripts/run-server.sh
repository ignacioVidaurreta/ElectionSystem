#!/bin/bash

echo "Server started"
cd ../server/target/election-system-server-1.0-SNAPSHOT/ || (echo "Error!"; exit 1)
./run-server.sh "$@"
