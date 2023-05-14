package site.doget.omok.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketDisconnectEventListener implements ApplicationListener<SessionDisconnectEvent> {

    private final SimpUserRegistry userRegistry;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    private final UserManager userManager;


    public WebSocketDisconnectEventListener(SimpMessagingTemplate messagingTemplate, SimpUserRegistry userRegistry, UserManager userManager) {
        this.userRegistry = userRegistry;
        this.messagingTemplate = messagingTemplate;
        this.userManager = userManager;
    }

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        // 세션 ID를 사용하여 접속 종료된 사용자의 정보를 얻음
        UserInfo simpUser = userManager.getUser(sessionId);
        String username = simpUser != null ? simpUser.getSender() : null;

        if (username != null) {
            // 접속 종료된 사용자를 리스트에서 제거
            userManager.removeUser(sessionId);

            // 접속 종료된 사용자를 다른 사용자에게 알리는 메시지 전송
            UserListMessage userListMessage = new UserListMessage();
            userListMessage.setSender("system");
            userListMessage.setUserList(UserManager.getAllUserList());
            sendMessageToTopic("/topic/chatParticipants", userListMessage);


        }
    }

    private void sendMessageToTopic(String topic, UserListMessage userListMessage) {
        messagingTemplate.convertAndSend(topic, userListMessage);
    }

}
