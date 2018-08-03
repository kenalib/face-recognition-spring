package hello;

import hello.service.RecognizedFace;
import org.springframework.util.Base64Utils;

import java.util.List;

public class FacePhoto {
    private List<RecognizedFace> faces;
    private String imageBase64;

    public FacePhoto(List<RecognizedFace> faces, byte[] photo) {
        this.faces = faces;
        this.imageBase64 = new String(Base64Utils.encode(photo));
    }

    public List<RecognizedFace> getFaces() {
        return faces;
    }

    public void setFaces(List<RecognizedFace> faces) {
        this.faces = faces;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }
}
