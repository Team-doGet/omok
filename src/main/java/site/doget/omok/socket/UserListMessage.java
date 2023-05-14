package site.doget.omok.socket;

import lombok.Data;

import java.util.List;

@Data
public class UserListMessage {

    private String sender;
    private List<UserInfo> userList;

}
