package site.doget.omok.Game;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {
    Optional<Game> findTopByWinnerAndPlayer1OrWinnerAndPlayer2OrderByIdDesc(String winner1, String player1, String winner2, String player2);

}
