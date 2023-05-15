package site.doget.omok.socket.match;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import site.doget.omok.game.Game;
import site.doget.omok.game.GameRepository;
import site.doget.omok.socket.chat.UserInfoDTO;
import site.doget.omok.socket.chat.UserManager;

import java.time.LocalDateTime;


@Controller
@RequiredArgsConstructor
public class MatchController {

    private final GameRepository gameRepository;
    private final MatchManager matchManager;
    private final UserManager userManager;

    @MessageMapping("/lobby/match")
    @SendTo("/topic/match")
    public MatchResponseDTO matchRequest(@Payload MatchMessageDTO matchMessageDTO, SimpMessageHeaderAccessor headerAccessor) {
        String userSessionId = headerAccessor.getSessionId();
        // 매칭 신청한 유저를 큐에 추가
        matchManager.addSocketId(userSessionId);
        if (matchManager.queueSize() == 2) {
            return createGame();
        }
        MatchResponseDTO matchResponseDTO = new MatchResponseDTO();
        matchMessageDTO.setSender("system");
        matchMessageDTO.setTime(LocalDateTime.now());
        return matchResponseDTO;
    }

    private MatchResponseDTO createGame() {
        String playerSessionId1 = matchManager.queuePoll();
        String playerSessionId2 = matchManager.queuePoll();
        UserInfoDTO player1 = userManager.getUser(playerSessionId1);
        UserInfoDTO player2 = userManager.getUser(playerSessionId2);

        Game game = new Game();
        game.setPlayer1(player1.getSender());
        game.setPlayer2(player2.getSender());
        game.setWinner("");
        this.gameRepository.save(game);

        MatchResponseDTO matchResponseDTO = new MatchResponseDTO();
        matchResponseDTO.setPlayer1(playerSessionId1);
        matchResponseDTO.setPlayer2(playerSessionId2);
        matchResponseDTO.setPlayerInfo1(player1);
        matchResponseDTO.setPlayerInfo2(player2);
        matchResponseDTO.setTime(LocalDateTime.now());
        matchResponseDTO.setSender("system");
        matchResponseDTO.setContent("경기가 시작됩니다.");
        return matchResponseDTO;
    }
}