package site.doget.omok.socket;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@Controller
public class PlayController {
    private final Map<String, Players> playersMap = new HashMap<>();

    //접속관리
    @SendTo("/topic/game/{gameId}")
    @MessageMapping("/game/{gameId}/join")
    public void handleGameJoin(@DestinationVariable String gameId, Players players, SimpMessageHeaderAccessor headerAccessor) {
        if (playersMap.get(gameId) == null) {
            playersMap.put(gameId, players);
        } else {
            String session1 = players.getPlayerSession1();
            if(!"".equals(session1)){
                playersMap.get(gameId).setPlayerSession1(players.getPlayerSession1());
            }
            String session2 = players.getPlayerSession2();
            if(!"".equals(session2)){
                playersMap.get(gameId).setPlayerSession2(players.getPlayerSession2());
            }
        }
        System.out.println(playersMap);
        System.out.println(playersMap.get(gameId));
    }
    //기권, 승리
//    @SendTo("/topic/game/{gameId}")
//    @MessageMapping("/game/{gameId}/put")
//    public void handleGame(@DestinationVariable String gameId, Player player, SimpMessageHeaderAccessor headerAccessor) {
//        playersMap.get(gameId);
//        System.out.println(player);
//    }
    //착수

}