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
    private final Map<String, PlayersRequestDTO> playersMap = new HashMap<>();
    private final OmokUserRepository userRepository;
    private final GameRepository gameRepository;

    @MessageMapping("/game/{gameId}/join")
    @SendTo("/topic/game/{gameId}")
    public void handleGameJoin(@DestinationVariable String gameId, PlayersRequestDTO playersRequest) {
        if (playersMap.get(gameId) == null) {
            playersMap.put(gameId, playersRequest);
        } else {
            String session1 = playersRequest.getPlayerSession1();
            if (!"".equals(session1)) {
                playersMap.get(gameId).setPlayerSession1(playersRequest.getPlayerSession1());
            }
            String session2 = playersRequest.getPlayerSession2();
            if (!"".equals(session2)) {
                playersMap.get(gameId).setPlayerSession2(playersRequest.getPlayerSession2());
            }
        }
    }

    @MessageMapping("/game/{gameId}/end")
    @SendTo("/topic/game/{gameId}/end")
    public PlayEndMessageDTO handleGame(@DestinationVariable String gameId, @Payload PlayEndMessageDTO playEndMessage, SimpMessageHeaderAccessor headerAccessor) {
        String lossPlayer = headerAccessor.getSessionId();
        PlayersRequestDTO playersRequestDTO = playersMap.get(gameId);
        String session1 = playersRequestDTO.getPlayerSession1();
        String playerName1 = playersRequestDTO.getPlayerName1();
        String playerName2 = playersRequestDTO.getPlayerName2();
        String lossPlayerName = lossPlayer.equals(session1) ? playerName1 : playerName2;
        String winPlayerName = !lossPlayer.equals(session1) ? playerName1 : playerName2;

        userRepository.incrementLossByUsername(lossPlayerName);
        userRepository.incrementWinByUsername(winPlayerName);
        gameRepository.setWinnerById(Long.parseLong(gameId), winPlayerName);
        playersMap.remove(gameId);
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