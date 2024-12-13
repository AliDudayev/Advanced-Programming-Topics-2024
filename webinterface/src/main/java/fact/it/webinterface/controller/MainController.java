package fact.it.webinterface.controller;

import fact.it.webinterface.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    private final TokenService tokenService;

    @Autowired
    public MainController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("tokenExists", tokenService.hasToken());
        return "homePage";  // Directing to the homePage template
    }

    @PostMapping("/saveToken")
    public String saveToken(@RequestParam("token") String token, Model model) {
        tokenService.setToken(token);
        model.addAttribute("message", "Token saved successfully!");
        model.addAttribute("tokenExists", tokenService.hasToken());
        return "homePage";  // After saving the token, return to the same page
    }
}
