package io.github.xanish.jackpot.kafka;

import io.github.xanish.jackpot.model.Bet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class BetProducerService {

    private static final Logger logger = LoggerFactory.getLogger(
        BetProducerService.class
    );

    private final KafkaTemplate<String, Bet> kafkaTemplate;
    private final String topicName;

    public BetProducerService(
        KafkaTemplate<String, Bet> kafkaTemplate,
        @Value("${jackpot.topic.name}") String topicName
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    public void sendBet(Bet bet) {
        try {
            logger.info(
                "Publishing bet to Kafka topic '{}': {}",
                topicName,
                bet
            );

            // Use jackpotId as key for partitioning
            kafkaTemplate.send(topicName, bet.getJackpotId(), bet);
        } catch (Exception e) {
            logger.error("Error publishing bet to Kafka: {}", bet, e);
            // TODO: Implement fallback or error handling (e.g., dead-letter queue)
        }
    }
}
