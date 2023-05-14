package site.doget.omok.socket;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Component
public class UserManager {
    private static final Map<String, UserInfo> users = new HashMap<>();

    // 접속자 정보 추가
    public static void addUser(String socketId, UserInfo user) {
        users.put(socketId, user);
    }

    // 접속자 정보 삭제
    public static void removeUser(String socketId) {
        users.remove(socketId);
    }

    // 접속자 정보 조회
    public static UserInfo getUser(String socketId) {
        return users.get(socketId);
    }

    // 모든 접속자 정보 반환
    public static Map<String, UserInfo> getAllUsers() {
        return users;
    }

    public static List<UserInfo> getAllUserList() {
        List<UserInfo> userList = new ArrayList<>(users.values());
        return userList;
    }
}