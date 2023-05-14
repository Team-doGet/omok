package site.doget.omok.socket;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import site.doget.omok.Game.Game;
import site.doget.omok.Game.GameRepository;

import java.time.LocalDateTime;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


@Controller
@CrossOrigin(origins = {"http://omok.doget.site", "58.122.202.23:80"})
public class ChatController {

    @Autowired
    private UserManager userManager;

    @Autowired
    private GameRepository gameRepository;
    private final Queue<String> matchQueue = new ConcurrentLinkedQueue<>();

    @MessageMapping("/lobby/join")
    @SendTo("/topic/chatParticipants")
    public UserListMessage joinChat(UserInfo userInfo, SimpMessageHeaderAccessor headerAccessor) throws JsonProcessingException {
        String username = userInfo.getSender();
        String userSessionId = headerAccessor.getSessionId();
        UserManager.addUser(userSessionId, userInfo);
        UserListMessage msg = new UserListMessage();
        msg.setSender("system");
        msg.setUserList(UserManager.getAllUserList());
        return msg;
    }

    @MessageMapping("/lobby/leave")
    @SendTo("/topic/chatParticipants")
    public UserListMessage leaveChat(UserInfo userInfo, SimpMessageHeaderAccessor headerAccessor) {
        String username = userInfo.getSender();
        String userSessionId = headerAccessor.getSessionId();
        UserManager.removeUser(userSessionId);
        UserListMessage msg = new UserListMessage();
        msg.setSender("system");
        msg.setUserList(UserManager.getAllUserList());
        return msg;
    }

    @MessageMapping("/lobby/chat")
    @SendTo("/topic/public") // 구독 중인 클라이언트에게 메시지 전송
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        chatMessage.setTime(LocalDateTime.now());
        return chatMessage;
    }

    @SendTo("/topic/public")
    @MessageMapping("/lobby/matchMessage")
    public ChatMessage matchMessage(@Payload ChatMessage matchRequest, UserInfo userInfo) {
        matchRequest.setSender("system");
        matchRequest.setTime(LocalDateTime.now());
        return matchRequest;
    }

    @SendTo("/topic/match")
    @MessageMapping("/lobby/match")
    public MatchResponse matchRequest(@Payload MatchRequest matchRequest, UserInfo userInfo, SimpMessageHeaderAccessor headerAccessor) {
        String userSessionId = headerAccessor.getSessionId();
        // 매칭 신청한 유저를 큐에 추가
        matchQueue.offer(userSessionId);
        if (matchQueue.size() == 2) {
            return createGame();
        }
        MatchResponse matchResponse = new MatchResponse();
        matchRequest.setSender("system");
        matchRequest.setTime(LocalDateTime.now());
        return matchResponse;
    }

    private MatchResponse createGame() {
        String playerSessionId1 = matchQueue.poll();
        String playerSessionId2 = matchQueue.poll();
        UserInfo player1 = UserManager.getUser(playerSessionId1);
        UserInfo player2 = UserManager.getUser(playerSessionId2);

        Game game = new Game();
        game.setPlayer1(player1.getSender());
        game.setPlayer2(player2.getSender());
        game.setWinner("");
        this.gameRepository.save(game);

        MatchResponse matchResponse = new MatchResponse();
        matchResponse.setPlayer1(playerSessionId1);
        matchResponse.setPlayer2(playerSessionId2);
        matchResponse.setPlayerInfo1(player1);
        matchResponse.setPlayerInfo2(player2);
        matchResponse.setTime(LocalDateTime.now());
        matchResponse.setSender("system");
        matchResponse.setContent("경기가 시작됩니다.");
        return matchResponse;
    }
}

