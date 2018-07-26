package hello.service;

import org.bytedeco.javacpp.aliface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
@EnableConfigurationProperties(ServiceProperties.class)
public class MyService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyService.class);

    @Value("classpath:aliface/configure.txt")
    private Resource aliFaceConfigResource;
    @Value("classpath:aliface/model/resnetsmall_5DB_2loss_140000_kaiya3.dat")
    private Resource aliFaceModelResource;
    @Value("classpath:aliface/visitor.db")
    private Resource aliFaceDBResource;

    private aliface.CIDRecognitionLocal recognitionLocal;
    private boolean recognitionLocalInitialized = false;
    private final Object aliFaceLock = new Object();

    private final ServiceProperties serviceProperties;

    public MyService(ServiceProperties serviceProperties) {
        this.serviceProperties = serviceProperties;
    }

    public String message() {
        LOGGER.info(recognitionLocal.toString());
        return this.serviceProperties.getMessage();
    }

    @PostConstruct
    public void initializeAliFace() throws IOException {
        synchronized (aliFaceLock) {
            _initializeAliFace();
        }
    }

    private void _initializeAliFace() throws IOException {
        String ALIFACE_DB_NAME = "visitor";
        recognitionLocal = aliface.CIDRecognitionLocal.GetInstance();

        InputStream configIS = aliFaceConfigResource.getInputStream();
        byte[] aliFaceConfigData = StreamUtils.copyToByteArray(configIS);
        recognitionLocal.LoadConfigure(aliFaceConfigData, aliFaceConfigData.length);

        InputStream modelIS = aliFaceModelResource.getInputStream();
        byte[] aliFaceModelData = StreamUtils.copyToByteArray(modelIS);
        if (recognitionLocal.Initialize(aliFaceModelData, aliFaceModelData.length) != 0) {
            LOGGER.error("Unable to initialize AliFace recognitionLocal.");
            throw new IOException("Initialize error");
        }

        recognitionLocalInitialized = true;

        InputStream dbIS = aliFaceDBResource.getInputStream();
        byte[] aliFaceDBData = StreamUtils.copyToByteArray(dbIS);
        File dbFile = File.createTempFile(ALIFACE_DB_NAME, ".db");
        FileOutputStream os = new FileOutputStream(dbFile);
        os.write(aliFaceDBData);

        recognitionLocal.CreateResetFaceDatabase(dbFile.getAbsolutePath().getBytes());
        recognitionLocal.LoadFaceDatabase(dbFile.getAbsolutePath().getBytes());
    }

    @PreDestroy
    public void uninitializeAliFace() {
        synchronized (aliFaceLock) {
            _uninitializeAliFace();
        }
    }

    private void _uninitializeAliFace() {
        if (recognitionLocalInitialized) {
            recognitionLocal.Uninitialize();
            aliface.CIDRecognitionLocal.ReleaseInstance(recognitionLocal);
        }
    }

}
