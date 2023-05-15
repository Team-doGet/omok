package site.doget.omok.socket.match;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class MatchMessageDTO {
    private String content;
    private String type;
    private String sender;
    private LocalDateTime time;
}
