package site.doget.omok.Game;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {
    Optional<Game> findTopByWinnerAndPlayer1OrWinnerAndPlayer2OrderByIdDesc(String winner1, String player1, String winner2, String player2);
    @Transactional
    default void setWinnerById(Long gameId, String winner) {
        findById(gameId).ifPresent(game -> {
            game.setWinner(winner);
            save(game);
        });
    }
}
