
## Compilation

```bash
# centos7 vm setup
vagrant plugin install vagrant-vbguest
vagrant box add centos/7
vagrant init centos/7
vagrant up
vagrant status

vagrant vbguest
vagrant vbguest --status
GuestAdditions 5.0.16 running --- OK.
vagrant reload

# install maven
vagrant ssh
sudo yum install -y java-1.8.0-openjdk-devel
curl -OL https://archive.apache.org/dist/maven/maven-3/3.5.4/binaries/apache-maven-3.5.4-bin.tar.gz
tar -xvzf apache-maven-3.5.4-bin.tar.gz
sudo mv apache-maven-3.5.4 /opt/maven
sudo sh -c 'echo "export PATH=/opt/maven/bin:\${PATH}" > /etc/profile.d/maven.sh'
source /etc/profile.d/maven.sh

# install gcc
sudo yum install -y gcc-c++

# compile
mvn clean install -DskipTests=true
mvn package -DskipTests
```

