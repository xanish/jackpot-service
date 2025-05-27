package io.github.xanish.jackpot.repositories;

import io.github.xanish.jackpot.model.JackpotReward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JackpotRewardRepository
    extends JpaRepository<JackpotReward, Long> {}
