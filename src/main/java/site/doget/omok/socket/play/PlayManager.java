package site.doget.omok.socket.play;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class PlayManager {
    private final Map<String, PlayersRequestDTO> play = new HashMap<>();

    // 게임 참가
    public void putGame(String gameId, PlayersRequestDTO playersRequest) {
        play.put(gameId, playersRequest);
    }

    // 참가자 관련
    public void setPlayer1SessionId(String gameId, String socketId) {
        play.get(gameId).setPlayerSession1(socketId);
    }

    public void setPlayer2SessionId(String gameId, String socketId) {
        play.get(gameId).setPlayerSession2(socketId);
    }

    public String getPlayerSessionId1(String gameId) {
        return play.get(gameId).getPlayerSession1();
    }

    public String getPlayerSessionId2(String gameId) {
        return play.get(gameId).getPlayerSession2();
    }

    public String getPlayerName1(String gameId) {
        return play.get(gameId).getPlayerName1();
    }

    public String getPlayerName2(String gameId) {
        return play.get(gameId).getPlayerName2();
    }

    // 게임 종료
    public void removeGame(String gameId) {
        play.remove(gameId);
    }

    // 게임 조회
    public PlayersRequestDTO getPlayersRequest(String gameId) {
        return play.get(gameId);
    }

    public String getGameId(String sessionId) {
        Set<String> keys = play.keySet();

        for (Map.Entry<String, PlayersRequestDTO> entry : play.entrySet()) {
            String key = entry.getKey();
            PlayersRequestDTO value = entry.getValue();
            if (value.getPlayerSession1().equals(sessionId) || value.getPlayerSession2().equals(sessionId)) {
                return key;
            }
        }
        return "";
    }
}