package fact.it.webinterface.controller;

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
    public String getAllHealthRecords(Model model) {
        model.addAttribute("healthRecords", healthService.getAllHealthRecords());
        return "healthPage";
    }

    @PostMapping("/addHealth")
    public String addHealthRecord(@RequestParam("workoutCode") String workoutCode,
                                  @RequestParam("recoveryHeartRate") String recoveryHeartRate,
                                  @RequestParam("bloodPressure") String bloodPressure,
                                  @RequestParam("caloriesBurned") String caloriesBurned,
                                  @RequestParam("oxygenSaturation") String oxygenSaturation,
                                  Model model) {
        Object newHealthRecord = new Object(); // Create and populate the health record
        healthService.createHealthRecord(newHealthRecord);
        model.addAttribute("message", "Health Record created successfully!");
        return "redirect:/health";
    }
}
