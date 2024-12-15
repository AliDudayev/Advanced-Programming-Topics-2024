package fact.it.webinterface.controller;

import fact.it.webinterface.dto.UserRequest;
import fact.it.webinterface.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Endpoint to display all users
    @GetMapping("/users")
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());  // Fetch all users from UserService
        return "userPage";  // Display on userPage template
    }

    // Endpoint to display a specific user's details
    @GetMapping("/user/{userCode}")
    public String getUserByCode(@PathVariable String userCode, Model model) {
        model.addAttribute("user", userService.getUser(userCode));  // Fetch a specific user from UserService
        return "userDetailPage";  // Display on userDetailPage template
    }

    // Endpoint to add a new user
    @PostMapping("/user")
    public String addUser(@RequestParam("name") String name,
                          @RequestParam("age") String age,
                          @RequestParam("height") String height,
                          @RequestParam("weight") String weight,
                          @RequestParam("fitnessGoals") String fitnessGoals,
                          Model model) {
        UserRequest userRequest = new UserRequest(name, age, height, weight, fitnessGoals);
        // Call UserService to create a new user
        userService.createUser(userRequest);
        model.addAttribute("message", "User added successfully!");  // Provide success message
        return "redirect:/users";  // Redirect to the list of users
    }

    // Endpoint to update an existing user
    @PostMapping("/user/update/{userCode}")
    public String updateUser(@PathVariable String userCode,
                             @RequestParam("name") String name,
                             @RequestParam("age") String age,
                             @RequestParam("height") String height,
                             @RequestParam("weight") String weight,
                             @RequestParam("fitnessGoals") String fitnessGoals,
                             Model model) {
        UserRequest userRequest = new UserRequest(name, age, height, weight, fitnessGoals);
        // Call UserService to update the user information
        userService.updateUser(userRequest);
        model.addAttribute("message", "User updated successfully!");  // Provide success message
        return "redirect:/users";  // Redirect to the list of users
    }

    // Endpoint to delete an existing user
    @PostMapping("/user/delete/{userCode}")
    public String deleteUser(@PathVariable String userCode, Model model) {
        // Call UserService to delete the user by userCode
        userService.deleteUser(userCode);
        model.addAttribute("message", "User deleted successfully!");  // Provide success message
        return "redirect:/users";  // Redirect to the list of users
    }
}
