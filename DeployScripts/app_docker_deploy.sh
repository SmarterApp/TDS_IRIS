#!/bin/bash
set -ev

cd docker/
echo Downloading from $CONTENT_PACKAGE_URL
mkdir content
wget -q -O content.zip "$CONTENT_PACKAGE_URL" 
unzip -o content.zip -d content/ &> /dev/null
rm content.zip 

docker build -t osucass/tds_iris:$BRANCH-content .
docker push osucass/tds_iris:$BRANCH-content
