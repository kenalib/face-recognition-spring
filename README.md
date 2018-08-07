
# Face Recognition Demo


## Quick Start

* run this if you have jar in target directory

```bash
# install docker https://www.docker.com/community-edition
docker-compose up
open http://localhost:8080/check.html
# to stop
# docker-compose down
```

* or build and run manually

```bash
docker build ./face-recognition-demo-server \
    -t kenali/face-recognition-demo-server \
    --build-arg JAR_FILE=target/face-recognition-demo-server-2.0.3.RELEASE.jar
docker run -p 8080:8080 --name face-server kenali/face-recognition-demo-server
```

## Build jar

* start virtual box vm and build and run jar on the vm

### setup vm

```bash
# setup vagrant and virtualbox
# https://www.vagrantup.com/
# https://www.virtualbox.org/wiki/Downloads
vagrant plugin list         # check if exists
vagrant plugin install vagrant-vbguest
vagrant init centos/7
```

```
# edit Vagrantfile
config.vm.network "private_network", ip: "192.168.33.10"
config.vm.synced_folder "/xxx/face-recognition-demo", "/demo"
config.vm.provider "virtualbox" do |vb|
  vb.memory = "8192"
end
config.vm.provision "shell", inline: <<-SHELL
  yum update -y
  timedatectl set-timezone Asia/Tokyo
SHELL
```

```bash
vagrant up
vagrant vbguest --status    # check the status
vagrant ssh

# https://github.com/bytedeco/javacpp-presets/wiki/Build-Environments
yum install epel-release
yum install clang gcc-c++ gcc-gfortran java-devel ant maven python numpy swig git file which wget unzip tar bzip2 gzip xz patch autoconf-archive automake make cmake3 libtool perl nasm yasm alsa-lib-devel freeglut-devel gtk2-devel libusb-devel libusb1-devel zlib-devel SDL-devel
yum install `rpm -qa | sed s/.x86_64$/.i686/`

# the "Prerequisites for all platforms" tasks
git clone https://github.com/bytedeco/javacpp.git
git clone https://github.com/bytedeco/javacpp-presets.git
cd javacpp
mvn clean install
```

### build jar

```bash
cd /demo/
./mvnw install      # or ./mvnw install -DskipTests

# run on virtual box vm
./mvnw spring-boot:run -pl face-recognition-demo-server
# or java -jar ./face-recognition-demo-server/target/face-recognition-demo-server-2.0.3.RELEASE.jar
open http://192.168.33.10:8080/

# to check DB
open http://192.168.33.10:8080/h2-console/
# default in-memory JDBC URL: jdbc:h2:mem:testdb
# or db file defined in application.properties
```

### run jar on some server

```bash
# on server, you need nohup
JARFILE=./face-recognition-demo-server/target/face-recognition-demo-server-2.0.3.RELEASE.jar
nohup java -jar $JARFILE > /tmp/output.log$$ 2>&1 &
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


## Deploy using Docker

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


## Update Data

```bash
curl -i -X POST -H "Content-Type:application/json" -d '{"name": "TESTING"}'
    http://192.168.33.10:8080/people

curl -i http://192.168.33.10:8080/people

curl -i -X DELETE -H "Content-Type:application/json" \
    http://192.168.33.10:8080/people/xxx

curl http://192.168.33.10:8080/people/search/findByName?name=Baggins
```


## CentOS7 HTTPS setup using Nginx and Let's Encrypts

### setup HTTP

* start vm and note its IP address
* create domain https://my.freenom.com/clientarea.php
* in this example, created `faceapp.tk` with the IP address

```bash
yum install -y epel-release
yum install -y nginx
nginx -v
# nginx version: nginx/1.15.2
systemctl start nginx
# check if it is working in http
open http://faceapp.tk/
```

### setup HTTPS

* create ssl certificate and setup nginx conf

```bash
# https://certbot.eff.org/lets-encrypt/centosrhel7-nginx
yum -y install yum-utils
yum-config-manager --enable rhui-REGION-rhel-server-extras rhui-REGION-rhel-server-optional
yum install python2-certbot-nginx
# update server_name at 2nd line in /etc/nginx/conf.d/default.conf
# server {
#     server_name faceapp.tk www.faceapp.tk;
certbot --nginx
# follow the guide
```

* Spring Boot and Nginx setup

```bash
# copy app jar to the server (sample command)
docker-machine scp ./face-recognition-demo-server/target/face-recognition-demo-server-2.0.3.RELEASE.jar face-server:/root/
# start Spring Boot app jar
java -jar face-recognition-demo-server-2.0.3.RELEASE.jar
```

```bash
# https://bach.mystories.vn/2017/12/24/secure-springboot-with-letsencrypt/
# update location / proxy_pass in /etc/nginx/conf.d/default.conf
#     location / {
#         proxy_pass http://127.0.0.1:8080;
#     }
systemctl restart nginx
# check if it is working in https
open https://faceapp.tk/
```

* in case you need apache httpd

```bash
yum install -y httpd
yum install -y mod_ssl
# edit /etc/httpd/conf.d/ssl.conf
# https://qiita.com/daiki_44/items/a3616390f277722b97e0
service httpd graceful
```


## Run Visual VM

* https://visualvm.github.io/download.html
* https://computingforgeeks.com/how-to-enable-and-use-ssh-x11-forwarding-on-vagrant-instances/


## Reference

* https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html
* https://spring.io/guides/gs/rest-service/
* https://spring.io/guides/gs/multi-module/
* https://spring.io/guides/gs/accessing-data-jpa/
* https://spring.io/guides/gs/accessing-data-rest/
* https://spring.io/guides/gs/rest-service-cors/
* https://www.slideshare.net/masatoshitada7/spring-data-jpa-jsug
* https://qiita.com/studioTeaTwo/items/071e6294cf6e7e17f201
* https://www.html5rocks.com/en/tutorials/getusermedia/intro/
* https://github.com/bytedeco/javacpp-presets/wiki/Build-Environments
