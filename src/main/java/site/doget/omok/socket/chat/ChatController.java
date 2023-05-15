package site.doget.omok.socket.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;


@Controller
@RequiredArgsConstructor
public class ChatController {

    private final UserManager userManager;

    @MessageMapping("/lobby/join")
    @SendTo("/topic/chatParticipants")
    public UserListDTO joinChat(UserInfoDTO userInfo, SimpMessageHeaderAccessor headerAccessor) {
        String userSessionId = headerAccessor.getSessionId();
        userManager.addUser(userSessionId, userInfo);
        UserListDTO userList = new UserListDTO();
        userList.setSender("system");
        userList.setUserList(userManager.getAllUserList());
        return userList;
    }

    @MessageMapping("/lobby/chat")
    @SendTo("/topic/public") // 구독 중인 클라이언트에게 메시지 전송
    public ChatMessageDTO sendMessage(@Payload ChatMessageDTO chatMessage) {
        chatMessage.setTime(LocalDateTime.now());
        return chatMessage;
    }

    @MessageMapping("/lobby/matchMessage")
    @SendTo("/topic/public")
    public ChatMessageDTO matchMessage(@Payload ChatMessageDTO matchMessage) {
        matchMessage.setSender("system");
        matchMessage.setTime(LocalDateTime.now());
        return matchMessage;
    }

}