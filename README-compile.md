
# Face Recognition Demo


## How to Compile

* start virtual box vm and build and run jar on the vm
* note that the face recognition sdk is not in this repository as in .gitignore


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
  vb.memory = "8192"    # or 4096
end
config.vm.provision "shell", inline: <<-SHELL
  yum update -y
  timedatectl set-timezone Asia/Tokyo
SHELL
```

```bash
vagrant up
vagrant vbguest --status    # check the status
# GuestAdditions 5.0.16 running --- OK.
```

### install maven

```bash
vagrant ssh

sudo yum install -y java-1.8.0-openjdk-devel
curl -OL https://archive.apache.org/dist/maven/maven-3/3.5.4/binaries/apache-maven-3.5.4-bin.tar.gz
tar -xvzf apache-maven-3.5.4-bin.tar.gz
sudo mv apache-maven-3.5.4 /opt/maven
sudo sh -c 'echo "export PATH=/opt/maven/bin:\${PATH}" > /etc/profile.d/maven.sh'
source /etc/profile.d/maven.sh
```

### install gcc

```bash
sudo yum install -y gcc-c++
```

### build jar

```bash
cd /demo/
./mvnw clean install      # or ./mvnw clean install -DskipTests

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
