package fact.it.webinterface.controller;

import fact.it.webinterface.dto.WorkoutRequest;
import fact.it.webinterface.service.WorkoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/workouts")
public class WorkoutController {

    private final WorkoutService workoutService;

    @Autowired
    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    // Get all workouts
    @GetMapping
    public String getAllWorkouts(Model model) {
        model.addAttribute("workouts", workoutService.getAllWorkouts());
        return "workoutPage";
    }

    // Add a new workout
    @PostMapping("/add")
    public String addWorkout(@RequestBody WorkoutRequest workoutRequest, Model model) {
        workoutService.createWorkout(workoutRequest);
        model.addAttribute("message", "Workout created successfully!");
        return "redirect:/workouts";
    }

    // Get a workout by a userCode
    @GetMapping("/user/{userCode}")
    public String getWorkoutByUserCode(@PathVariable String userCode, Model model) {
        model.addAttribute("workout", workoutService.getWorkoutByUserCode(userCode));
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
