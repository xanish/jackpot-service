package io.github.xanish.jackpot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.xanish.jackpot.models.JackpotContribution;

@Repository
public interface JackpotContributionRepository extends JpaRepository<JackpotContribution, Long> {
}
