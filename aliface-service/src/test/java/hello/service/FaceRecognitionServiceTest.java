package hello.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest("service.message=Hello")
public class FaceRecognitionServiceTest {

    @Autowired
    private FaceRecognitionService faceRecognitionService;

    @Test
    public void contextLoads() throws IOException {
        faceRecognitionService.initializeAliFace();
    }

    @SpringBootApplication
    static class TestConfiguration {
    }

}
