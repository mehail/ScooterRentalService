CALL mvn clean install -Dmaven.test.skip=true

DEL docker\*.jar
DEL docker\*.war

COPY core\target\*.jar .\docker
COPY web\target\*.war .\docker

CD docker
CALL docker-compose up

PAUSE