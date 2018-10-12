
# Face Recognition Demo (backend)

NOTE: Files in aliface-wrapper directory are not in this repo since it is Alibaba product.
Therefore some of maven dependencies will not be resolved.

This is backend. The frontend is here -> https://github.com/kenalib/face-recognition-angular


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

* need to compile on centos7 (on virtual box vm)

[How to Compile](README-compile.md)


## Deploy using Docker

[Deploy Using Docker](README-docker.md)


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
* https://spring.io/guides/gs/testing-web/
* https://www.slideshare.net/masatoshitada7/spring-data-jpa-jsug
* https://qiita.com/studioTeaTwo/items/071e6294cf6e7e17f201
* https://www.html5rocks.com/en/tutorials/getusermedia/intro/
* https://github.com/bytedeco/javacpp-presets/wiki/Build-Environments
