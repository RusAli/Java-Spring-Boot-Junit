package org.myapp.kafka;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.protocol.types.Field;
import org.awaitility.Awaitility;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.SendResult;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@AllArgsConstructor
public class KafkaService {

    private KafkaTemplate<String, String> kafkaTemplate;
    private List<ConsumerRecord<String, String>> consumerRecords;

    @Step("Send message to kafka")
    public void sendMessage(String message) {
        CompletableFuture<SendResult<String, String>> send = kafkaTemplate.send("test-topic", UUID.randomUUID().toString(), message);
        Awaitility.await("Await sending message")
                .logging(log::info)
                .atMost(10, TimeUnit.SECONDS)
                .until(send::isDone);
    }

    @KafkaListener(topics = "test-topic", groupId = "test")
    public void listen(ConsumerRecord<String, String> consumerRecord, Acknowledgment ack) {

        Allure.addAttachment("message", consumerRecord.value());
        consumerRecords.add(consumerRecord);
        ack.acknowledge();
    }

    @Step("Receive message")
    public ConsumerRecord<String, String> getRecord(){
        Awaitility.await("Await receiving message")
                .logging(log::info)
                .atMost(10,TimeUnit.SECONDS)
                .until(() -> !consumerRecords.isEmpty());
        return consumerRecords.get(0);
    }

}
