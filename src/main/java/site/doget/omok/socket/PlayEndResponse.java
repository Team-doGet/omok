package site.doget.omok.socket;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PlayEndResponse {
    private String winner;
    private String stone;
    private String content;
    private String sender;

}
