package site.doget.omok.socket;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import site.doget.omok.Game.GameRepository;
import site.doget.omok.user.OmokUserRepository;

import java.util.HashMap;
import java.util.Map;

@Controller
public class PlayController {
    private final Map<String, Players> playersMap = new HashMap<>();
    private final OmokUserRepository userRepository;
    private final GameRepository gameRepository;

    public PlayController(OmokUserRepository userRepository, GameRepository gameRepository) {
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
    }

    //접속관리
    @SendTo("/topic/game/{gameId}")
    @MessageMapping("/game/{gameId}/join")
    public void handleGameJoin(@DestinationVariable String gameId, Players players, SimpMessageHeaderAccessor headerAccessor) {
        if (playersMap.get(gameId) == null) {
            playersMap.put(gameId, players);
        } else {
            String session1 = players.getPlayerSession1();
            if (!"".equals(session1)) {
                playersMap.get(gameId).setPlayerSession1(players.getPlayerSession1());
            }
            String session2 = players.getPlayerSession2();
            if (!"".equals(session2)) {
                playersMap.get(gameId).setPlayerSession2(players.getPlayerSession2());
            }
        }
    }

    //기권, 승리
    @SendTo("/topic/game/{gameId}/end")
    @MessageMapping("/game/{gameId}/end")
    public PlayEndResponse handleGame(@DestinationVariable String gameId, @Payload PlayEndResponse playEndResponse, SimpMessageHeaderAccessor headerAccessor) {
        String lossPlayer = headerAccessor.getSessionId();
        Players players = playersMap.get(gameId);
        String lossPlayerName = players.getPlayerSession1().equals(lossPlayer)? players.getPlayerName1():players.getPlayerName2();
        String winPlayerName = lossPlayerName.equals(players.getPlayerName1())? players.getPlayerName2():players.getPlayerName1();
        userRepository.incrementLossByUsername(lossPlayerName);
        userRepository.incrementWinByUsername(winPlayerName);

        gameRepository.setWinnerById(Long.parseLong(gameId),winPlayerName);
        playersMap.remove(gameId);
        playEndResponse.setWinner(winPlayerName);
        playEndResponse.setSender("system");
        playEndResponse.setContent("게임 종료");
        return playEndResponse;
    }

    //착수
    @SendTo("/topic/game/{gameId}")
    @MessageMapping("/game/{gameId}/setStone")
    public PlayMessage handleGamePut(PlayMessage playMessage) {
        return playMessage;
    }
}