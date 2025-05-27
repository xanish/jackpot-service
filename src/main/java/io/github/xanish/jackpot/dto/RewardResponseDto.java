package io.github.xanish.jackpot.dto;

import io.github.xanish.jackpot.model.JackpotReward;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RewardResponseDto {

    private String betId;
    private String userId;
    private String jackpotId;
    private BigDecimal jackpotRewardAmount;
    private LocalDateTime createdAt;
    private String message;

    public static RewardResponseDto fromJackpotReward(
        JackpotReward reward,
        String message
    ) {
        RewardResponseDto dto = new RewardResponseDto();
        dto.setBetId(reward.getBetId());
        dto.setUserId(reward.getUserId());
        dto.setJackpotId(reward.getJackpotId());
        dto.setJackpotRewardAmount(reward.getJackpotRewardAmount());
        dto.setCreatedAt(reward.getCreatedAt());
        dto.setMessage(message);

        return dto;
    }
}
