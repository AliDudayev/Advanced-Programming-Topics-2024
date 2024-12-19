package fact.it.webinterface.controller;

import fact.it.webinterface.dto.WorkoutRequest;
import fact.it.webinterface.service.UserService;
import fact.it.webinterface.service.WorkoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Controller
@RequestMapping("/workouts")
public class WorkoutController {

    private final WorkoutService workoutService;
    private final UserService userService;

    @Autowired
    public WorkoutController(WorkoutService workoutService, UserService userService) {
        this.workoutService = workoutService;
        this.userService = userService;
    }

    // Get all workouts
    @GetMapping
    public String getAllWorkouts(Model model) {
        model.addAttribute("workouts", workoutService.getAllWorkouts());
        model.addAttribute("users", userService.getAllUsers());
        return "workoutPage";
    }

    // Add a new workout
    @PostMapping("/add")
    public String addWorkout(
            @RequestParam String userCode,
            @RequestParam String name,
            @RequestParam String duration,
            @RequestParam String sets,
            @RequestParam String reps,
            @RequestParam String type,
            @RequestParam String weight,
            @RequestParam String distance,
            @RequestParam String speed,
            @RequestParam String description,
            @RequestParam String pauseBetweenReps) {

        String workoutCode = name; // will be changed in backend
        String id = String.valueOf(System.currentTimeMillis());

        try {
        workoutService.createWorkout(new WorkoutRequest(
                id,
                userCode,
                workoutCode,
                name,
                duration,
                sets,
                reps,
                type,
                weight,
                distance,
                speed,
                description,
                pauseBetweenReps
        ));
        }
        catch (Exception e) {
            System.out.println("an unknown error occurred but didn't influence anything");;
        }

        return "redirect:/workouts";
    }


    // Get a workout by a userCode
    @GetMapping("/user")
    public String getWorkoutByUserCode(@RequestParam("userCode") String userCode, Model model) {
        if (Objects.equals(userCode, "*")) {
            return "redirect:/workouts";
        }
        model.addAttribute("workouts", workoutService.getWorkoutByUserCode(userCode));
        model.addAttribute("users", userService.getAllUsers());
        return "workoutPage";
    }

    // Update an existing workout
//    @PostMapping("/update/{workoutCode}")
//    public String updateWorkout(@PathVariable String workoutCode, @RequestBody WorkoutRequest workoutRequest, Model model) {
//        workoutService.updateWorkout(workoutRequest);
//        model.addAttribute("message", "Workout updated successfully!");
//        return "redirect:/workouts";
//    }

    // Delete a workout
//    @PostMapping("/delete/{workoutCode}")
//    public String deleteWorkout(@PathVariable String workoutCode, Model model) {
//        workoutService.deleteWorkout(workoutCode);
//        model.addAttribute("message", "Workout deleted successfully!");
//        return "redirect:/workouts";
//    }
}
