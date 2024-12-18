package fact.it.webinterface.controller;

import fact.it.webinterface.dto.UserRequest;
import fact.it.webinterface.service.TokenService;
import fact.it.webinterface.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final TokenService tokenService;

    @Autowired
    public UserController(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    // Endpoint to display all users
    @GetMapping
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "userPage";
    }

    // Endpoint to add a new user
    @PostMapping("/add")
    public String addUser(
            @RequestParam String userCode,
            @RequestParam String name,
            @RequestParam String age,
            @RequestParam String height,
            @RequestParam String weight,
            @RequestParam String fitnessGoals) {

        UserRequest userRequest = new UserRequest(userCode, name, age, height, weight, fitnessGoals);
        userService.createUser(userRequest);

        return "redirect:/users";
    }

    // Endpoint to edit user details
    @GetMapping("/edit")
    public String editUser(@RequestParam String userCode, Model model) {
        model.addAttribute("user", userService.getUser(userCode));
        return "editUser";
    }

    // Endpoint to update an existing user
    @PostMapping("/update")
    public String updateUser(
            @RequestParam String userCode,
            @RequestParam String name,
            @RequestParam String age,
            @RequestParam String height,
            @RequestParam String weight,
            @RequestParam String fitnessGoals) {

        UserRequest userRequest = new UserRequest(userCode, name, age, height, weight, fitnessGoals);
        userService.updateUser(userRequest);

        return "redirect:/users";
    }

    // Endpoint to delete an existing user
    @PostMapping("/delete")
    public String deleteUser(@RequestParam String userCode) {
        String token = tokenService.getToken();
        if (token == null) {
            return "redirect:/error";
        } else {
            userService.deleteUser(userCode);
            return "redirect:/users";
        }
    }
}
