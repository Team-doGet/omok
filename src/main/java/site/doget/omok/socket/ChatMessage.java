package site.doget.omok.socket;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {

    private String content;
    private String sender;
}
