package site.doget.omok.socket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;


@Controller
@CrossOrigin(origins = {"http://omok.doget.site","58.122.202.23:80"})
public class ChatController {

    @MessageMapping("/chat.sendMessage") // 클라이언트로부터 메시지 수신
    @SendTo("/topic/public") // 구독 중인 클라이언트에게 메시지 전송
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/chat.addUser") // 새로운 사용자 접속 처리
    @SendTo("/topic/public") // 구독 중인 클라이언트에게 메시지 전송
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        // WebSocket 세션에 사용자명을 연결
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }
}

