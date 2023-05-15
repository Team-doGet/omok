package site.doget.omok.socket.chat;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UserListDTO {
    private String sender;
    private List<UserInfoDTO> userList;
}
