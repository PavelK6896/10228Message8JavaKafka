package app.web.pavelk.message8;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext //сбросить контекст
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
class EmbeddedKafkaTest {

    @Autowired
    public KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private KafkaConsumer1 kafkaConsumer1;

    @Autowired
    private KafkaProducer1 kafkaProducer1;

    @Value("${test.topic}")
    private String topic;

    @Test
    void whenDefaultTemplate() throws InterruptedException {
        kafkaTemplate.send(topic, "Sending with default template");
        kafkaConsumer1.getLatch().await(10000, TimeUnit.MILLISECONDS);
        assertThat(kafkaConsumer1.getLatch().getCount(), equalTo(0L));
        
        assertThat(kafkaConsumer1.getPayload(), containsString("embedded-test-topic"));
    }

    @Test
    void whenSimpleProducer() throws InterruptedException {
        kafkaProducer1.send(topic, "Sending with our own simple KafkaProducer");
        kafkaConsumer1.getLatch().await(10000, TimeUnit.MILLISECONDS);
        
        assertThat(kafkaConsumer1.getLatch().getCount(), equalTo(0L));
        assertThat(kafkaConsumer1.getPayload(), containsString("embedded-test-topic"));
    }

}
