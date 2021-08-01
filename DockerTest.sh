#!/bin/bash

mvn clean package -DskipTests

rm -r ./docker/*.jar ./docker/*.war
cp core/target/*.jar ./docker/
cp web/target/*.war ./docker/


sudo docker rm srs
sudo docker rmi scooter-rental-service

cd docker

sudo docker build -t scooter-rental-service .
sudo docker run -it --name srs -p 8080:8080 scooter-rental-service

read -n1 -r -p "Press any key to continue..."