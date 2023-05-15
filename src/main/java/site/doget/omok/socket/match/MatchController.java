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
    public MatchResponseDTO matchRequest(@Payload MatchMessageDTO matchMessage, SimpMessageHeaderAccessor headerAccessor) {
        String socketId = headerAccessor.getSessionId();
        if ("start".equals(matchMessage.getType())) {
            // 매칭 신청한 유저를 큐에 추가
            if (matchManager.queueSize() > 0) {
                UserInfoDTO player1 = userManager.getUser(socketId);
                String playerSocketId2 = matchManager.queuePeek();
                UserInfoDTO player2 = userManager.getUser(playerSocketId2);
                String playerName1 = player1.getSender();
                String playerName2 = player2.getSender();
                if (!playerName1.equals(playerName2)) {
                    return createGame(socketId, player1);
                }
            }
            matchManager.addSocketId(socketId);
        }
        if ("cancel".equals(matchMessage.getType())) {
            matchManager.removeSocketId(socketId);
        }

        MatchResponseDTO matchResponse = new MatchResponseDTO();
        matchResponse.setSender("system");
        matchResponse.setTime(LocalDateTime.now());
        return matchResponse;
    }

    private MatchResponseDTO createGame(String playerSocketId1, UserInfoDTO player1) {
        String playerSocketId2 = matchManager.queuePoll();
        UserInfoDTO player2 = userManager.getUser(playerSocketId2);

        Game game = new Game();
        game.setPlayer1(player1.getSender());
        game.setPlayer2(player2.getSender());
        game.setWinner("");
        this.gameRepository.save(game);

        MatchResponseDTO matchResponseDTO = new MatchResponseDTO();
        matchResponseDTO.setPlayer1(playerSocketId1);
        matchResponseDTO.setPlayer2(playerSocketId2);
        matchResponseDTO.setPlayerInfo1(player1);
        matchResponseDTO.setPlayerInfo2(player2);
        matchResponseDTO.setTime(LocalDateTime.now());
        matchResponseDTO.setSender("system");
        matchResponseDTO.setContent("경기가 시작됩니다.");
        return matchResponseDTO;
    }
}