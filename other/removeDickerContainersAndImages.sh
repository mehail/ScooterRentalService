#!/bin/bash

# shellcheck disable=SC2046
docker stop $(docker ps -aq)
docker rm $(docker ps -aq)
docker rmi -f $(docker images -aq)