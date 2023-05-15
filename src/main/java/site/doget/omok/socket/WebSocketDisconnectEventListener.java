package site.doget.omok.socket;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import site.doget.omok.socket.chat.UserInfoDTO;
import site.doget.omok.socket.chat.UserListDTO;
import site.doget.omok.socket.chat.UserManager;
import site.doget.omok.socket.match.MatchManager;

@Component
@RequiredArgsConstructor
public class WebSocketDisconnectEventListener implements ApplicationListener<SessionDisconnectEvent> {

    private final SimpMessagingTemplate messagingTemplate;
    private final UserManager userManager;
    private final MatchManager matchManager;

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        // 세션 ID를 사용하여 접속 종료된 사용자의 정보를 얻음
        UserInfoDTO simpUser = userManager.getUser(sessionId);

        // 접속 종료된 사용자 매치큐에서 제거
        matchManager.removeSocketId(sessionId);

        String username = simpUser != null ? simpUser.getSender() : null;
        if (username != null) {
            // 접속 종료된 사용자를 리스트에서 제거
            userManager.removeUser(sessionId);

            // 접속 종료된 사용자를 다른 사용자에게 알리는 메시지 전송
            UserListDTO userList = new UserListDTO();
            userList.setSender("system");
            userList.setUserList(userManager.getAllUserList());
            sendMessageToTopic("/topic/chatParticipants", userList);
        }
    }

    private void sendMessageToTopic(String topic, UserListDTO userListDTO) {
        messagingTemplate.convertAndSend(topic, userListDTO);
    }

}
