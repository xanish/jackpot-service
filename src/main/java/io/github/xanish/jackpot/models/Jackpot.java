package io.github.xanish.jackpot.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

import io.github.xanish.jackpot.enums.ContributionType;
import io.github.xanish.jackpot.enums.RewardType;

@Entity
@Table(name = "jackpots")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Jackpot {
    @Id
    private String jackpotId;
    private BigDecimal currentPoolAmount;
    private BigDecimal initialPoolAmount;

    @Enumerated(EnumType.STRING)
    private ContributionType contributionType;
    private BigDecimal contributionValue; // Percentage for fixed, initial for variable

    @Enumerated(EnumType.STRING)
    private RewardType rewardType;
    private BigDecimal rewardValue; // Chance percentage for fixed, initial for variable
    private BigDecimal rewardPoolLimit; // For variable chance, when it becomes 100%
}
