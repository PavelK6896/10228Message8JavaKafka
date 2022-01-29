package app.web.pavelk.message8.controller;

import app.web.pavelk.message8.KafkaProducer1;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MainController {

    private final KafkaProducer1 kafkaProducer1;

    @GetMapping("/one")
    public String one() {
        kafkaProducer1.send("embedded-test-topic", "one one -- 11 ");
        return "one";
    }

}
