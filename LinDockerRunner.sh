#!/bin/bash

mvn clean install -Dmaven.test.skip=true

rm -r ./docker/*.jar ./docker/*.war
cp core/target/*.jar ./docker/
cp web/target/*.war ./docker/

sudo docker-compose -f docker/docker-compose.yml up

read -n1 -r -p "Press any key to continue..."