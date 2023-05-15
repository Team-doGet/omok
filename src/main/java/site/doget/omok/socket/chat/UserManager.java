package site.doget.omok.socket.chat;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserManager {
    private final Map<String, UserInfoDTO> users = new HashMap<>();

    // 접속자 추가
    public void addUser(String socketId, UserInfoDTO user) {
        users.put(socketId, user);
    }

    // 접속자 삭제
    public void removeUser(String socketId) {
        users.remove(socketId);
    }

    // 접속자 조회
    public UserInfoDTO getUser(String socketId) {
        return users.get(socketId);
    }

    // 모든 접속자 반환
    public List<UserInfoDTO> getAllUserList() {
        List<UserInfoDTO> userList = new ArrayList<>(users.values());
        return userList;
    }
}