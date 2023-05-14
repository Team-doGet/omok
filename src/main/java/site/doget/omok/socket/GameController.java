package site.doget.omok.socket;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class GameController {

    @MessageMapping("/game/{gameId}/move")
    @SendTo("/topic/game/{gameId}")
    public GameMessage handleGameMove(@DestinationVariable String gameId, GameMessage move) {
        // 1:1 게임 진행 처리 로직
        // 게임 상태 업데이트 등의 작업을 수행할 수 있습니다.
        return move;
    }
}