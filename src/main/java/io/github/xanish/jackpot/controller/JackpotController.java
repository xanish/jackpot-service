package io.github.xanish.jackpot.controller;

import io.github.xanish.jackpot.dto.BetAcceptedResponseDto;
import io.github.xanish.jackpot.dto.BetRequestDto;
import io.github.xanish.jackpot.dto.RewardEvaluationRequestDto;
import io.github.xanish.jackpot.dto.RewardResponseDto;
import io.github.xanish.jackpot.kafka.BetProducerService;
import io.github.xanish.jackpot.model.Bet;
import io.github.xanish.jackpot.model.JackpotReward;
import io.github.xanish.jackpot.service.JackpotService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/jackpots")
public class JackpotController {

    private static final Logger logger = LoggerFactory.getLogger(
        JackpotController.class
    );

    private final BetProducerService betProducerService;
    private final JackpotService jackpotService;

    public JackpotController(
        BetProducerService betProducerService,
        JackpotService jackpotService
    ) {
        this.betProducerService = betProducerService;
        this.jackpotService = jackpotService;
    }

    @PostMapping("/bets")
    public ResponseEntity<String> publishBet(
        @Valid @RequestBody BetRequestDto betRequestDto
    ) {
        Bet bet = new Bet(
            betRequestDto.getBetId(),
            betRequestDto.getUserId(),
            betRequestDto.getJackpotId(),
            betRequestDto.getBetAmount()
        );
        betProducerService.sendBet(bet);

        logger.info("Bet publish request received and sent to Kafka: {}", bet);

        BetAcceptedResponseDto acceptedResponse = new BetAcceptedResponseDto(
            System.currentTimeMillis(),
            HttpStatus.ACCEPTED.value(),
            "Bet received and queued for processing."
        );

        return new ResponseEntity<>(acceptedResponse, HttpStatus.ACCEPTED);
    }

    @PostMapping("/bets/evaluate-reward")
    public ResponseEntity<RewardResponseDto> evaluateReward(
        @Valid @RequestBody RewardEvaluationRequestDto rewardEvaluationRequestDto
    ) {
        logger.info(
            "Reward evaluation request received for bet: {}",
            rewardEvaluationRequestDto.getBetId()
        );

        Optional<JackpotReward> rewardOpt = jackpotService.evaluateBetForReward(
            rewardEvaluationRequestDto.getBetId(),
            rewardEvaluationRequestDto.getUserId(),
            rewardEvaluationRequestDto.getJackpotId()
        );

        if (rewardOpt.isPresent()) {
            return ResponseEntity.ok(
                RewardResponseDto.fromJackpotReward(
                    rewardOpt.get(),
                    "Congratulations! You won the jackpot."
                )
            );
        } else {
            RewardResponseDto noWinResponse = new RewardResponseDto();
            noWinResponse.setBetId(rewardEvaluationRequestDto.getBetId());
            noWinResponse.setUserId(rewardEvaluationRequestDto.getUserId());
            noWinResponse.setJackpotId(
                rewardEvaluationRequestDto.getJackpotId()
            );
            noWinResponse.setMessage(
                "Sorry, this bet did not win a jackpot reward."
            );

            return ResponseEntity.ok(noWinResponse);
        }
    }
}
