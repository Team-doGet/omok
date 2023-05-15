package site.doget.omok.socket;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayMessage

{
    private String stone;
    private Integer x;
    private Integer y;
    private Integer turn;
    private String sender;

}
