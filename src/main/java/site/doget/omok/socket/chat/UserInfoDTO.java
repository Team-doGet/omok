package site.doget.omok.socket.chat;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserInfoDTO {
    private String content;
    private String sender;
    private String loss;
    private String win;
}
