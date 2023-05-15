package site.doget.omok.socket;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import site.doget.omok.game.GameRepository;
import site.doget.omok.socket.chat.UserInfoDTO;
import site.doget.omok.socket.chat.UserListDTO;
import site.doget.omok.socket.chat.UserManager;
import site.doget.omok.socket.match.MatchManager;
import site.doget.omok.socket.play.PlayEndMessageDTO;
import site.doget.omok.socket.play.PlayManager;
import site.doget.omok.socket.play.PlayersRequestDTO;
import site.doget.omok.user.OmokUserRepository;

@Component
@RequiredArgsConstructor
public class WebSocketDisconnectEventListener implements ApplicationListener<SessionDisconnectEvent> {

    private final SimpMessagingTemplate messagingTemplate;
    private final UserManager userManager;
    private final MatchManager matchManager;
    private final PlayManager playManager;
    private final OmokUserRepository userRepository;
    private final GameRepository gameRepository;

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        // 세션 ID를 사용하여 접속 종료된 사용자의 정보를 얻음
        UserInfoDTO simpUser = userManager.getUser(sessionId);

        // 접속 종료된 사용자 매치큐에서 제거
        matchManager.removeSocketId(sessionId);
        String gameId = playManager.getGameId(sessionId);
        if (gameId != "") {
            String lossPlayer = sessionId;
            String session1 = playManager.getPlayerSessionId1(gameId);
            String playerName1 = playManager.getPlayerName1(gameId);
            String playerName2 = playManager.getPlayerName2(gameId);
            String lossPlayerName = lossPlayer.equals(session1) ? playerName1 : playerName2;
            String winPlayerName = !lossPlayer.equals(session1) ? playerName1 : playerName2;

            userRepository.incrementLossByUsername(lossPlayerName);
            userRepository.incrementWinByUsername(winPlayerName);
            gameRepository.setWinnerById(Long.parseLong(gameId), winPlayerName);

            playManager.removeGame(gameId);
            PlayEndMessageDTO playEndMessage = new PlayEndMessageDTO();
            playEndMessage.setWinner(winPlayerName);
            playEndMessage.setSender("system");
            playEndMessage.setContent("게임 종료");
            sendMessageToTopic("/topic/game/"+gameId+"/end", playEndMessage);

        }

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

    private void sendMessageToTopic(String topic, Object message) {
        messagingTemplate.convertAndSend(topic, message);
    }
}
