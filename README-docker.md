
# Face Recognition Demo


## Deploy Using Docker

* prepare docker environment and run using docker

### prepare docker vm

* create vm and install docker (maybe skip if you use docker on Mac)

```bash
# simply prepare centos7 vm and run the below on it
sudo yum install docker
# sudo systemctl stop firewalld
sudo systemctl status docker
sudo systemctl start docker
```

```bash
# or create vm using docker-machine
# https://github.com/AliyunContainerService/docker-machine-driver-aliyunecs
export ECS_ACCESS_KEY_ID=xxxxx
export ECS_ACCESS_KEY_SECRET=xxxxxxxxxx
export ECS_REGION=ap-southeast-1
export ECS_INSTANCE_TYPE=ecs.r5.large

docker-machine create \
    --driver aliyunecs \
    --aliyunecs-image-id centos_7_04_64_20G_alibase_201701015.vhd \
    face-server

docker-machine ls
# you should see a new vm, so try ssh to it
docker-machine ssh face-server
```

### build docker using the jar

```bash
# tag name is in pom.xml
sudo ./mvnw -DskipTests dockerfile:build -pl face-recognition-demo-server
# or use docker-compose build
# or build manually
# sudo docker build ./face-recognition-demo-server \
#   -t kenali/face-recognition-demo-server \
#   --build-arg JAR_FILE=./target/face-recognition-demo-server-2.0.3.RELEASE.jar
```

### run container on the vm

```bash
eval $(docker-machine env face-server)
docker-compose up
eval $(docker-machine env -u)

# or if you run it without docker-compose on the vm
sudo docker run -p 8080:8080 -t kenali/face-recognition-demo-server

# for debug
sudo docker run -it --entrypoint sh --rm kenali/face-recognition-demo-server
```

