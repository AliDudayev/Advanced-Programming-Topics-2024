package fact.it.webinterface.controller;

import fact.it.webinterface.dto.UserRequest;
import fact.it.webinterface.service.TokenService;
import fact.it.webinterface.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
    @RequestMapping
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "userPage";
    }

    // Endpoint to add a new user
    @RequestMapping("/add")
    public String addUser(Model model, HttpServletRequest request) {
        String userCode = request.getParameter("userCode");
        String name = request.getParameter("name");
        String age = request.getParameter("age");
        String height = request.getParameter("height");
        String weight = request.getParameter("weight");
        String fitnessGoals = request.getParameter("fitnessGoals");

        UserRequest userRequest = new UserRequest(userCode, name, age, height, weight, fitnessGoals);
        userService.createUser(userRequest);

        return "redirect:/users";
    }

    @RequestMapping("/edit")
    public String editUser(Model model, HttpServletRequest request) {
        String userCode = request.getParameter("userCode");
        model.addAttribute("user", userService.getUser(userCode));
        return "editUser";
    }

    // Endpoint to update an existing user
    @RequestMapping("/update")
    public String updateUser(HttpServletRequest request) {
        String userCode = request.getParameter("userCode");
        String name = request.getParameter("name");
        String age = request.getParameter("age");
        String height = request.getParameter("height");
        String weight = request.getParameter("weight");
        String fitnessGoals = request.getParameter("fitnessGoals");

        UserRequest userRequest = new UserRequest(userCode, name, age, height, weight, fitnessGoals);
        userService.updateUser(userRequest);

        return "redirect:/users";
    }

    // Endpoint to delete an existing user
    @RequestMapping("/delete")
    public String deleteUser(HttpServletRequest request) {
        String userCode = request.getParameter("userCode");
        String token = tokenService.getToken();
        if (token == null) {
            return "redirect:/error";
        } else {
            userService.deleteUser(userCode);
            return "redirect:/users";
        }
    }
}
