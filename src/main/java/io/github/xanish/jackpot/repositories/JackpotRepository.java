package io.github.xanish.jackpot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.xanish.jackpot.models.Jackpot;

@Repository
public interface JackpotRepository extends JpaRepository<Jackpot, String> {
}
