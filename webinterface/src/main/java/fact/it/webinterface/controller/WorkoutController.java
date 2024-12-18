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
    public String addWorkout(
            @RequestParam String userCodeAdd,
            @RequestParam String name,
            @RequestParam String date,
            @RequestParam String duration,
            @RequestParam String sets,
            @RequestParam String reps,
            @RequestParam String type,
            @RequestParam String weight,
            @RequestParam String distance,
            @RequestParam String speed,
            @RequestParam String description,
            @RequestParam String pauseBetweenReps) {

        String workoutCode = "will be generated in service";
        String id = String.valueOf(System.currentTimeMillis());
        workoutService.createWorkout(new WorkoutRequest(id, userCodeAdd, workoutCode, name, date, duration, sets, reps, type, weight, distance, speed, description, pauseBetweenReps));
        return "redirect:/workouts";
    }

    // Get a workout by a userCode
    @GetMapping("/user")
    public String getWorkoutByUserCode(@RequestParam("userCode") String userCode, Model model) {
        model.addAttribute("workouts", workoutService.getWorkoutByUserCode(userCode));
        return "redirect:/workouts";
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
