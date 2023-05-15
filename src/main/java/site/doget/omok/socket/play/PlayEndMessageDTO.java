package site.doget.omok.socket.play;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PlayEndMessageDTO {
    private String winner;
    private String stone;
    private String content;
    private String sender;
}
