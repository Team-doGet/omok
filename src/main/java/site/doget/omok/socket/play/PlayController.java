package site.doget.omok.socket.play;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import site.doget.omok.game.GameRepository;
import site.doget.omok.user.OmokUserRepository;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class PlayController {
    private final PlayManager playersMap;
    private final OmokUserRepository userRepository;
    private final GameRepository gameRepository;

    @MessageMapping("/game/{gameId}/join")
    @SendTo("/topic/game/{gameId}")
    public void handleGameJoin(@DestinationVariable String gameId, PlayersRequestDTO playersRequest) {
        if (playersMap.getPlayersRequest(gameId) == null) {
            playersMap.putGame(gameId, playersRequest);
        } else {
            String session1 = playersRequest.getPlayerSession1();
            if (!"".equals(session1)) {
                playersMap.getPlayersRequest(gameId).setPlayerSession1(playersRequest.getPlayerSession1());
            }
            String session2 = playersRequest.getPlayerSession2();
            if (!"".equals(session2)) {
                playersMap.getPlayersRequest(gameId).setPlayerSession2(playersRequest.getPlayerSession2());
            }
        }
    }

    @MessageMapping("/game/{gameId}/end")
    @SendTo("/topic/game/{gameId}/end")
    public PlayEndMessageDTO handleGame(@DestinationVariable String gameId, @Payload PlayEndMessageDTO playEndMessage, SimpMessageHeaderAccessor headerAccessor) {
        String lossPlayer = headerAccessor.getSessionId();
        String session1 = playersMap.getPlayerSessionId1(gameId);
        String playerName1 = playersMap.getPlayerName1(gameId);
        String playerName2 = playersMap.getPlayerName2(gameId);
        String lossPlayerName = lossPlayer.equals(session1) ? playerName1 : playerName2;
        String winPlayerName = !lossPlayer.equals(session1) ? playerName1 : playerName2;

        userRepository.incrementLossByUsername(lossPlayerName);
        userRepository.incrementWinByUsername(winPlayerName);
        gameRepository.setWinnerById(Long.parseLong(gameId), winPlayerName);

        playersMap.removeGame(gameId);
        playEndMessage.setWinner(winPlayerName);
        playEndMessage.setSender("system");
        playEndMessage.setContent("게임 종료");
        return playEndMessage;
    }

    @MessageMapping("/game/{gameId}/setStone")
    @SendTo("/topic/game/{gameId}")
    public PlayMessageDTO handleGamePut(PlayMessageDTO playMessage) {
        return playMessage;
    }
}