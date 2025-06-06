package io.github.xanish.jackpot.repository;

import io.github.xanish.jackpot.model.JackpotContribution;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JackpotContributionRepository
    extends JpaRepository<JackpotContribution, Long> {
    Optional<JackpotContribution> findByBetId(String betId);
}
