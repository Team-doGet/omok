package site.doget.omok.game;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class GameController {
    private final GameRepository gameRepository;

    @GetMapping("/play")
    public String getPlay(Model model, @AuthenticationPrincipal OAuth2User principal) {
        String username = principal.getAttribute("login");
        Optional<Game> og = gameRepository.findTopByWinnerAndPlayer1OrWinnerAndPlayer2OrderByIdDesc("", username, "", username);
        if (og.isPresent()) {
            Game game = og.get();
            model.addAttribute("user", username);
            model.addAttribute("game", game);
            return "play";
        }
        return "redirect:/";
    }
}
