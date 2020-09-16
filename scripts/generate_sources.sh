#!/bin/bash

set -e

cd ..

echo "🗳   Building Elections Project! Please hold... "

echo "CLEAN INSTALL STEP"
mvn clean install || (echo "ERROR! Aborting ..." && exit 1)

VERSION="1.0-SNAPSHOT"

echo "UNPACKAGE STEP"

# Uncompress generated resources
CLIENT_TAR="client/target/election-system-client-${VERSION}-bin.tar.gz"
SERVER_TAR="server/target/election-system-server-${VERSION}-bin.tar.gz"
tar -xvf ${CLIENT_TAR} -C client/target
tar -xvf ${SERVER_TAR} -C server/target

# Give execution permission to user
echo "PERMISSIONS STEP"

# Client permissions
CLIENT_BASE_PATH="client/target/election-system-client-${VERSION}"
chmod u+x ${CLIENT_BASE_PATH}/run-management.sh
chmod u+x ${CLIENT_BASE_PATH}/run-vote.sh
chmod u+x ${CLIENT_BASE_PATH}/run-fiscal.sh
chmod u+x ${CLIENT_BASE_PATH}/run-query.sh

#Server permissions
SERVER_BASE_PATH="server/target/election-system-server-${VERSION}"
chmod u+x ${SERVER_BASE_PATH}/run-registry.sh
chmod u+x ${SERVER_BASE_PATH}/run-server.sh

echo "🚀 All Done! Have fun"


