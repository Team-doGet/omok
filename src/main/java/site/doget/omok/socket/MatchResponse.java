package site.doget.omok.socket;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MatchResponse {

    private String content;
    private Integer room;
    private String player1;
    private String player2;
    private UserInfo playerInfo1;
    private UserInfo playerInfo2;
    private String sender;
    private LocalDateTime time;

}
