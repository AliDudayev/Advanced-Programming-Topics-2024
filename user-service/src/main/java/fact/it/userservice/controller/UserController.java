package fact.it.userservice.controller;

import fact.it.userservice.dto.RecordResponse;
import fact.it.userservice.dto.UserRequest;
import fact.it.userservice.dto.UserResponse;
import fact.it.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Constructor;
import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Create a new user --> Klaar
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void createUser
            (@RequestBody UserRequest userRequest) {
        userService.createUser(userRequest);
    }

    // Get a single user by code  --> Klaar
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getUserByCode(@RequestParam String userCode) {
        return userService.getUserByCode(userCode);
    }

    // Get all users  --> Klaar
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    // Update a user  --> isMale werkt niet.
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void updateUser(@RequestParam String userCode, @RequestBody UserRequest userRequest) {
        userService.updateUser(userCode, userRequest);
    }

    //Delete a user by code  --> Klaar
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@RequestParam String userCode) {
        userService.deleteUser(userCode);
    }

    // Delete a user  --> Klaar
//    @DeleteMapping
//    @ResponseStatus(HttpStatus.OK)
//    public void deleteUser
//    (@RequestParam String userCode) {
//        return userService.deleteUser(userCode);
//    }

    // add the getRecords
//    @GetMapping("/records/all")
//    @ResponseStatus(HttpStatus.OK)
//    public RecordResponse getAllRecords(@RequestParam String userCode) {
//        System.out.println("in Controller");
//        return userService.getAllRecords(userCode);
//    }
}

