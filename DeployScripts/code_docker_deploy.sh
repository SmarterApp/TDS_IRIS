#!/bin/bash
set -ev

cd docker/
docker build -f Dockerfile.code -t osucass/tds_iris:$BRANCH .
docker push osucass/tds_iris:$BRANCH