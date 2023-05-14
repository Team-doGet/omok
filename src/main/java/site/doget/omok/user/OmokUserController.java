package site.doget.omok.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class OmokUserController {

    private final OmokUserRepository userRepository;

    @Autowired
    public OmokUserController(OmokUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String index(Model model, @AuthenticationPrincipal OAuth2User principal) {
        if (principal != null) {
            String username = principal.getAttribute("login");
            OmokUser user = userRepository.findByUsername(username);
            if (user == null) {
                user = new OmokUser();
                user.setUsername(username);
                user.setEmail(principal.getAttribute("email"));
                user.setLoss(0L);
                user.setWin(0L);
                userRepository.save(user);
            }
            model.addAttribute("user", user);
        }

        return "index";
    }
    @GetMapping("/play")
    public String getPlay(Model model, @AuthenticationPrincipal OAuth2User principal){
        String username = principal.getAttribute("login");
        OmokUser user = userRepository.findByUsername(username);
        model.addAttribute("user", user);
        return "play";
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }

}