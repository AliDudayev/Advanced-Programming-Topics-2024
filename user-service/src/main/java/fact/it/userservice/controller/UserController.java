package fact.it.userservice.controller;

import fact.it.userservice.dto.RecordResponse;
import fact.it.userservice.dto.UserRequest;
import fact.it.userservice.dto.UserResponse;
import fact.it.userservice.dto.WorkoutResponse;
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

    // get record of specific user  --> Klaar
    @GetMapping("/record")
    @ResponseStatus(HttpStatus.OK)
    public RecordResponse getRecords(@RequestParam String userCode) {
        return userService.getRecordOfUser(userCode);
    }

    // get all records  --> Klaar
    @GetMapping("/record/all")
    @ResponseStatus(HttpStatus.OK)
    public List<RecordResponse> getAllRecords() {
        return userService.getAllRecords();
    }

    // Change a record of a specific user --> Klaar
    @PutMapping("/record")
    @ResponseStatus(HttpStatus.OK)
    public void updateRecord(@RequestParam String userCode, @RequestBody RecordResponse recordResponse) {
        userService.updateRecord(userCode, recordResponse);
    }

    // create a record for a specific user --> Klaar
    @PostMapping("/record")
    @ResponseStatus(HttpStatus.OK)
    public void createRecord(@RequestParam String userCode, @RequestBody RecordResponse recordResponse) {
        userService.createRecord(userCode, recordResponse);
    }

    // create a record for a specific user --> Klaar
    @DeleteMapping("/record")
    @ResponseStatus(HttpStatus.OK)
    public void deleteRecord(@RequestParam String userCode) {
        userService.deleteRecord(userCode);
    }

    // get all workouts from user --> Klaar
    @GetMapping("/workout")
    @ResponseStatus(HttpStatus.OK)
    public List<WorkoutResponse> getAllWorkouts(@RequestParam String userCode) {
        return userService.getAllWorkoutsFromUser(userCode);
    }
}

