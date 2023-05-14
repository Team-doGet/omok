package site.doget.omok.socket;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MatchRequest {

    private String content;
    private String type;
    private String sender;
    private LocalDateTime time;

}
