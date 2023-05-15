package site.doget.omok.socket.match;

import lombok.Getter;
import lombok.Setter;
import site.doget.omok.socket.chat.UserInfoDTO;

import java.time.LocalDateTime;

@Setter
@Getter
public class MatchResponseDTO {
    private String content;
    private String player1;
    private String player2;
    private UserInfoDTO playerInfo1;
    private UserInfoDTO playerInfo2;
    private String sender;
    private LocalDateTime time;
}
