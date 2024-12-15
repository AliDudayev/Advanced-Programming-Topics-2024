package fact.it.webinterface.controller;

import fact.it.webinterface.dto.WorkoutRequest;
import fact.it.webinterface.service.WorkoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WorkoutController {

    private final WorkoutService workoutService;

    @Autowired
    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @GetMapping("/workouts")
    public String getAllWorkouts(Model model) {
        model.addAttribute("workouts", workoutService.getAllWorkouts());
        return "workoutPage";
    }

    @PostMapping("/addWorkout")
    public String addWorkout(@RequestParam("workoutCode") String workoutCode,
                             @RequestParam("name") String name,
                             @RequestParam("date") String date,
                             @RequestParam("duration") String duration,
                             @RequestParam("sets") String sets,
                             @RequestParam("reps") String reps,
                             @RequestParam("type") String type,
                             @RequestParam("weight") String weight,
                             @RequestParam("distance") String distance,
                             @RequestParam("speed") String speed,
                             @RequestParam("description") String description,
                             Model model) {
        // Create a WorkoutRequest object using the constructor
        WorkoutRequest workoutRequest = new WorkoutRequest(
                workoutCode, name, date, duration, sets, reps, type, weight, distance, speed, description
        );

        // Use WorkoutService to handle the creation
        workoutService.createWorkout(workoutRequest);
        model.addAttribute("message", "Workout created successfully!");
        return "redirect:/workouts";
    }
}
