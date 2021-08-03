#!/bin/bash

mvn clean install -Dmaven.test.skip=true

rm -r ./docker/*.jar ./docker/*.war
cp core/target/*.jar ./docker/
cp web/target/*.war ./docker/

sudo docker rm srs
sudo docker rm srs_db
#sudo docker rmi scooter-rental-service

cd docker

docker-compose up

read -n1 -r -p "Press any key to continue..."