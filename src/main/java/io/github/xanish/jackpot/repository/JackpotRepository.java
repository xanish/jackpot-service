package io.github.xanish.jackpot.repository;

import io.github.xanish.jackpot.model.Jackpot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JackpotRepository extends JpaRepository<Jackpot, String> {}
