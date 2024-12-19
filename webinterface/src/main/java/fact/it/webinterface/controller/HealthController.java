package fact.it.webinterface.controller;

import fact.it.webinterface.dto.HealthRequest;
import fact.it.webinterface.service.HealthService;
import fact.it.webinterface.service.WorkoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Controller
@RequestMapping("/health")
public class HealthController {

    private final HealthService healthService;
    private final WorkoutService workoutService;

    @Autowired
    public HealthController(HealthService healthService, WorkoutService workoutService) {
        this.healthService = healthService;
        this.workoutService = workoutService;
    }

    // Endpoint to display all health metrics
    @GetMapping
    public String getAllHealthMetrics(Model model) {
        model.addAttribute("healthMetrics", healthService.getAllHealth());
        model.addAttribute("workouts", workoutService.getAllWorkouts());
        return "healthPage";
    }

    // Endpoint to get health metrics by workout code
    @GetMapping("/user")
    public String getHealthMetricsByUser(@RequestParam("workoutCode") String workoutCode, Model model) {
        if (Objects.equals(workoutCode, "*")) {
            return "redirect:/health";
        }
        model.addAttribute("healthMetrics", healthService.getHealth(workoutCode));
        return "healthPage";
    }

    @RequestMapping("/add")
    public String addHealthMetric(
            @RequestParam String recoveryHeartRate,
            @RequestParam String bloodPressure,
            @RequestParam("workoutCodeAdd") String workoutCode,
            @RequestParam String caloriesBurned,
            @RequestParam String oxygenSaturation) {

        String id = String.valueOf(System.currentTimeMillis());
        healthService.createHealth(new HealthRequest(id, recoveryHeartRate, bloodPressure, workoutCode, caloriesBurned, oxygenSaturation));
        return "redirect:/health";
    }



    @GetMapping("/edit")
    public String editHealthMetric(@RequestParam("workoutCode") String workoutCode, Model model) {
        model.addAttribute("health", healthService.getHealth(workoutCode));
        return "editHealth";
    }

    // Endpoint to update an existing health metric
    @PostMapping("/update")
    public String updateHealthMetric(
            @RequestParam String recoveryHeartRate,
            @RequestParam String bloodPressure,
            @RequestParam String workoutCode,
            @RequestParam String caloriesBurned,
            @RequestParam String oxygenSaturation) {

        HealthRequest old = healthService.getHealth(workoutCode);
        String id = old.getId();
        healthService.updateHealth(new HealthRequest(id, recoveryHeartRate, bloodPressure, workoutCode, caloriesBurned, oxygenSaturation));
        return "redirect:/health";
    }
}
