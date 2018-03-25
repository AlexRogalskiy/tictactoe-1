package ris58h.tictactoe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ris58h.tictactoe.domain.Game;

public interface GameRepository extends JpaRepository<Game, Long> {
}
