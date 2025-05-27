package io.github.xanish.jackpot.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "jackpot_rewards")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JackpotReward {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String betId;
    private String userId;
    private String jackpotId;
    private BigDecimal jackpotRewardAmount;
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
