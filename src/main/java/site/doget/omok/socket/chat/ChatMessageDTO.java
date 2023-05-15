package site.doget.omok.socket.chat;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ChatMessageDTO {
    private String sender;
    private String content;
    private LocalDateTime time;
}
