package fact.it.userservice.controller;

import fact.it.userservice.dto.RecordResponse;
import fact.it.userservice.dto.UserRequest;
import fact.it.userservice.dto.UserResponse;
import fact.it.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping // Het ophalen van een record op basis van een id
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getUserById
            (@RequestParam String id) {
        return userService.getUserById(id);
    }

    @PutMapping// Find the record by id and update it
    @ResponseStatus(HttpStatus.OK)
    public void updateUser(@RequestParam String userCode, @RequestBody UserRequest userRequest) {
        userService.updateUser(userCode, userRequest);
    }

    // add the getRecords
    @GetMapping("/records/all")
    @ResponseStatus(HttpStatus.OK)
    public RecordResponse[] getAllRecords() {
        return userService.getAllRecords();
    }
}

