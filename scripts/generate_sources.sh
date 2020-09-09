#!/bin/bash

cd ..

echo "CLEAN INSTALL STEP"
mvn clean install

echo "UNPACKAGE STEP"
# Uncompress generated resources
tar -xvf client/target/election-system-client-1.0-SNAPSHOT-bin.tar.gz -C client/target

echo "PERMISSIONS STEP"
chmod u+x client/target/election-system-client-1.0-SNAPSHOT/run-management.sh
chmod u+x client/target/election-system-client-1.0-SNAPSHOT/run-vote.sh
chmod u+x client/target/election-system-client-1.0-SNAPSHOT/run-fiscal.sh
echo "Ok"
