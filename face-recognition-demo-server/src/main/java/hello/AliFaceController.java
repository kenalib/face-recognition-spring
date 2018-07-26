package hello;

import hello.service.MyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AliFaceController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AliFaceController.class);
    private final MyService myService;

    public AliFaceController(MyService myService) {
        this.myService = myService;
    }

    @GetMapping("/")
    public String home() {
        return myService.message();
    }

}
