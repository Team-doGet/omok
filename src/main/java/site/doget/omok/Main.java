package site.doget.omok;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@CrossOrigin(origins = {"http://omok.doget.site","58.122.202.23:80"})
public class Main {
    @GetMapping("/index")
    public String getMain(){
        return "index";
    }



    @GetMapping("chatting")
    public String getLogin(){
        return "chat";
    }
}
