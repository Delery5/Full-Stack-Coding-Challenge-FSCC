package com.ibm.delery.registrationservice.kafka;

import com.ibm.delery.registrationservice.entity.Registration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {

    private KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(String topic, String employee) {

        kafkaTemplate.send(topic,employee);
    }
}
