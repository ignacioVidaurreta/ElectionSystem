#!/bin/bash

echo "Registry started..."
cd ../server/target/election-system-server-1.0-SNAPSHOT/ || (echo "Error!"; exit 1)
./run-registry.sh "$@"
