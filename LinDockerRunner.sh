#!/bin/bash

mvn clean install -Dmaven.test.skip=true

rm -r ./docker/*.jar ./docker/*.war
cp core/target/*.jar ./docker/
cp web/target/*.war ./docker/

# shellcheck disable=SC2164
cd docker

sudo docker-compose up

read -n1 -r -p "Press any key to continue..."