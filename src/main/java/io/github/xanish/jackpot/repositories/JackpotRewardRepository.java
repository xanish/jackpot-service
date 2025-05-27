package io.github.xanish.jackpot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.xanish.jackpot.models.JackpotReward;

@Repository
public interface JackpotRewardRepository extends JpaRepository<JackpotReward, Long> {
}
