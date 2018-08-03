
# Face Recognition Demo


## Quick Start

```bash
# setup vagrant and virtualbox
# https://www.vagrantup.com/
# https://www.virtualbox.org/wiki/Downloads
vagrant plugin list         # check if exists
vagrant plugin install vagrant-vbguest
vagrant vbguest --status    # check the status
vagrant init centos/7
```

```
# edit Vagrantfile
config.vm.network "private_network", ip: "192.168.33.10"
config.vm.synced_folder "/face-recognition-demo", "/demo"
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
vagrant ssh
cd /demo/
./mvnw install      # or ./mvnw install -DskipTests
./mvnw spring-boot:run -pl face-recognition-demo-server
# or java -jar ./face-recognition-demo-server/target/face-recognition-demo-server-2.0.3.RELEASE.jar
open http://192.168.33.10:8080/

# to check DB
open http://192.168.33.10:8080/h2-console/
# default in-memory JDBC URL: jdbc:h2:mem:testdb
# or db file defined in application.properties
```

## Docker

```bash
sudo yum install docker
sudo systemctl status docker
sudo systemctl start docker

# ./mvnw -DskipTests -Djavacpp.platform=linux-x86_64 install
# ./mvnw -DskipTests -Djavacpp.platform=linux-x86_64 dockerfile:build -pl face-recognition-demo-server
sudo ./mvnw -DskipTests dockerfile:build -pl face-recognition-demo-server

# or manual build
# sudo docker build ./face-recognition-demo-server/ -t kenali/face-recognition-demo-server --build-arg JAR_FILE=./target/face-recognition-demo-server-2.0.3.RELEASE.jar

sudo docker run -p 8080:8080 -t kenali/face-recognition-demo-server

# for debug
sudo docker run -it --entrypoint sh --rm kenali/face-recognition-demo-server
```


## Create Maven project

```
mvn -B archetype:generate -DgroupId=com.example -DartifactId=face-recognition-demo
#   optional (default values)
#  -DarchetypeGroupId=org.apache.maven.archetypes
#  -DarchetypeArtifactId=maven-archetype-quickstart
cd face-recognition-demo
mkdir src/main/resources
mvn package
java -cp target/face-recognition-demo-1.0-SNAPSHOT.jar com.example.App
```


## Use Spring Boot

```bash
# after add stuff in https://spring.io/guides/gs/rest-service/
mvn spring-boot:run
curl http://localhost:8080/greeting
```

select menu `Build | Build Module 'face-recognition-demo'`


## H2 database

```bash
http://192.168.33.10:8080/h2-console/
# the default is in-memory db
# JDBC URL: jdbc:h2:mem:testdb
# then Connect

# or file db (c.f. application.properties)
curl -O http://www.h2database.com/h2-2018-03-18.zip
unzip h2-2018-03-18.zip
cd h2/bin
bash h2.sh
open http://192.168.33.10:8082  # (vm ip address)
# JDBC URL: jdbc:h2:file:~/face-recognition-demo-h2
```


## Update Data

```bash
curl -i -X POST -H "Content-Type:application/json" -d '{"name": "TESTING"}'
    http://192.168.33.10:8080/people

curl -i http://192.168.33.10:8080/people
curl -i http://192.168.33.10:8080/people/1

curl -i -X DELETE -H "Content-Type:application/json" \
    http://192.168.33.10:8080/people/1

curl http://192.168.33.10:8080/people/search/findByName?name=Baggins
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
* https://www.slideshare.net/masatoshitada7/spring-data-jpa-jsug
