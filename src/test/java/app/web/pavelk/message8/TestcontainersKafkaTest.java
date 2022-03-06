package app.web.pavelk.message8;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(initializers = {TestcontainersKafkaTest.Initializer.class})
class TestcontainersKafkaTest {

    @Container
    static KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.0.1"));

    @Autowired
    public KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private KafkaConsumer1 kafkaConsumer1;

    @Autowired
    private KafkaProducer1 kafkaProducer1;

    @Value("${test.topic}")
    private String topic;

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.kafka.bootstrap-servers=" + kafkaContainer.getHost() + ":" + kafkaContainer.getFirstMappedPort()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @Test
    void whenDefaultTemplate() throws InterruptedException {
        System.out.println(kafkaContainer.getHost());
        System.out.println(kafkaContainer.getFirstMappedPort());

        kafkaTemplate.send(topic, "//kafkaTemplate// Sending with default template");
        kafkaConsumer1.getLatch().await(10000, TimeUnit.MILLISECONDS);
        assertThat(kafkaConsumer1.getLatch().getCount(), equalTo(0L));
        assertThat(kafkaConsumer1.getPayload(), containsString(topic));
    }

    @Test
    void whenSimpleProducer() throws InterruptedException {
        kafkaProducer1.send(topic, "//kafkaProducer1// Sending with our own simple KafkaProducer");
        kafkaConsumer1.getLatch().await(10000, TimeUnit.MILLISECONDS);
        assertThat(kafkaConsumer1.getLatch().getCount(), equalTo(0L));
        assertThat(kafkaConsumer1.getPayload(), containsString(topic));
    }

}