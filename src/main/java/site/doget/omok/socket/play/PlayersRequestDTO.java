package site.doget.omok.socket.play;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PlayersRequestDTO {
    private String playerName1;
    private String playerName2;
    private String playerSession1;
    private String playerSession2;
}
