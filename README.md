# LiquibaseGenerator

### Run in docker container
```
Run this commands in client folder:
docker build -t vuefront .
docker run -p 8080:8080 -d vuefront

Run this commands in server folder:
docker build -t server .
docker run -p 9000:9000 -d server
```
####or use docker-compose, but before run bootJar task
