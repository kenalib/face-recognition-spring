
# Face Recognition Demo


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


## Reference

* https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html
* https://spring.io/guides/gs/rest-service/
