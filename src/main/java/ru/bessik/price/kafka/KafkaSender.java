package ru.bessik.price.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import ru.bessik.price.kafka.dto.TelegramBotMessage;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaSender {

    private final ObjectMapper objectMapper;
    private final KafkaProperty kafkaProperty;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendTelegramBotMessage(TelegramBotMessage message) {
        try {
            CompletableFuture<SendResult<String, String>> future =
                    kafkaTemplate.send(kafkaProperty.getTopics().getTelegramBotSend(),
                            objectMapper.writeValueAsString(message));

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("send kafka message {} with offset {}", message, result.getRecordMetadata().offset());
                } else {
                    log.error("unable to send message {}, due to {}", message, ex.getMessage());
                }
            });
        } catch (JsonProcessingException e) {
            log.error("message {} error deserialization to string", message);
            throw new RuntimeException(e);
        }
    }
}
