package site.doget.omok.socket.play;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayMessageDTO {
    private String stone;
    private Integer x;
    private Integer y;
    private Integer turn;
    private String sender;
}
