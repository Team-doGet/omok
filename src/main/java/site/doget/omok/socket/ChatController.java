package site.doget.omok.socket;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.socket.WebSocketSession;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


@Controller
@CrossOrigin(origins = {"http://omok.doget.site", "58.122.202.23:80"})
public class ChatController {

    @Autowired
    private UserManager userManager;
    private int gameRoom = 1;

    @MessageMapping("/lobby/join")
    @SendTo("/topic/chatParticipants")
    public UserListMessage joinChat(UserInfo userInfo, SimpMessageHeaderAccessor headerAccessor) throws JsonProcessingException {
        String username = userInfo.getSender();
        String userSessionId = headerAccessor.getSessionId();
        userManager.addUser(userSessionId, userInfo);
        UserListMessage msg = new UserListMessage();
        msg.setSender("system");
        msg.setUserList(userManager.getAllUserList());
        return msg;
    }

    @MessageMapping("/lobby/leave")
    @SendTo("/topic/chatParticipants")
    public UserListMessage leaveChat(UserInfo userInfo, SimpMessageHeaderAccessor headerAccessor) {
        String username = userInfo.getSender();
        String userSessionId = headerAccessor.getSessionId();
        userManager.removeUser(userSessionId);
        UserListMessage msg = new UserListMessage();
        msg.setSender("system");
        msg.setUserList(userManager.getAllUserList());
        return msg;
    }
    @MessageMapping("/lobby/chat")
    @SendTo("/topic/public") // 구독 중인 클라이언트에게 메시지 전송
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        chatMessage.setTime(LocalDateTime.now());
        return chatMessage;
    }

    private Queue<String> matchQueue = new ConcurrentLinkedQueue<>();

    @SendTo("/topic/public")
    @MessageMapping("/lobby/matchMessage")
    public ChatMessage matchMessage(@Payload ChatMessage matchRequest,UserInfo userInfo){
        matchRequest.setSender("system");
        matchRequest.setTime(LocalDateTime.now());
        return matchRequest;
    }
}

