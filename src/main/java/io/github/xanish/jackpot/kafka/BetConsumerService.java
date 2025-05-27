package io.github.xanish.jackpot.kafka;

import io.github.xanish.jackpot.models.Bet;
import io.github.xanish.jackpot.services.JackpotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class BetConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(
        BetConsumerService.class
    );
    private final JackpotService jackpotService;

    public BetConsumerService(JackpotService jackpotService) {
        this.jackpotService = jackpotService;
    }

    @KafkaListener(
        topics = "${jackpot.topic.name}",
        groupId = "${spring.kafka.consumer.group-id}",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeBet(@Payload Bet bet) {
        logger.info("Consumed bet from Kafka: {}", bet);
        try {
            jackpotService.processBetContribution(bet);
        } catch (Exception e) {
            logger.error("Error processing consumed bet {}: ", bet, e);
            // TODO: Handle processing errors (e.g., move to an error topic, retry logic)
        }
    }
}
