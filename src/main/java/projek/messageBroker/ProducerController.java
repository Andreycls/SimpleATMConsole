package projek.messageBroker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProducerController {

    private Producer producer;
    @Autowired
    public ProducerController(Producer producer) {
        this.producer = producer;
    }

    @PostMapping("/send")
    public String sendMsg(@RequestBody String msg){
        System.out.println(msg);
        producer.produceMessage(msg);
        return "Done";
    }
}
