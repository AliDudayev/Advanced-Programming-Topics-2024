package fact.it.webinterface.controller;

import fact.it.webinterface.dto.HealthRequest;
import fact.it.webinterface.service.HealthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HealthController {

    private final HealthService healthService;

    @Autowired
    public HealthController(HealthService healthService) {
        this.healthService = healthService;
    }

    @GetMapping("/health")
    public String getAllHealthMetrics(Model model) {
        model.addAttribute("healthMetrics", healthService.getAllHealthRecords());
        return "healthPage";
    }

    @GetMapping("/health/user")
    public String getHealthMetricsByUser(@RequestParam("workoutCode") String workoutCode, Model model) {
        model.addAttribute("healthMetrics", healthService.getHealthRecord(workoutCode));
        return "healthPage";
    }

    @PostMapping("/addHealthMetric")
    public String addHealthMetric(@RequestParam("recoveryHeartRate") String recoveryHeartRate,
                                  @RequestParam("bloodPressure") String bloodPressure,
                                  @RequestParam("workoutCode") String workoutCode,
                                  @RequestParam("caloriesBurned") String caloriesBurned,
                                  @RequestParam("oxygenSaturation") String oxygenSaturation,
                                  Model model) {
        HealthRequest healthRequest = new HealthRequest(recoveryHeartRate, bloodPressure, workoutCode, caloriesBurned, oxygenSaturation);
        healthService.createHealthRecord(healthRequest);
        model.addAttribute("message", "Health metric added successfully!");
        return "redirect:/health";
    }
}
