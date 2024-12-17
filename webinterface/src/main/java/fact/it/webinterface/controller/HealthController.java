package fact.it.webinterface.controller;

import fact.it.webinterface.dto.HealthRequest;
import fact.it.webinterface.service.HealthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/health")
public class HealthController {

    private final HealthService healthService;

    @Autowired
    public HealthController(HealthService healthService) {
        this.healthService = healthService;
    }

    // Endpoint to display all health metrics
    @RequestMapping
    public String getAllHealthMetrics(Model model) {
        model.addAttribute("healthMetrics", healthService.getAllHealth());
        return "healthPage";
    }

    // Endpoint to get health metrics by workout code
    @RequestMapping("/user/{workoutCode}")
    public String getHealthMetricsByUser(@PathVariable String workoutCode, Model model) {
        model.addAttribute("healthMetrics", healthService.getHealth(workoutCode));
        return "healthPage";
    }

    // Endpoint to add a new health metric
    @RequestMapping("/add")
    public String addHealthMetric(@RequestBody HealthRequest healthRequest, Model model) {
        healthService.createHealth(healthRequest);
        model.addAttribute("message", "Health metric added successfully!");
        return "redirect:/health";
    }

    // Endpoint to update an existing health metric
    @RequestMapping("/update")
    public String updateHealthMetric(@RequestBody HealthRequest healthRequest, Model model) {
        healthService.updateHealth(healthRequest);
        model.addAttribute("message", "Health metric updated successfully!");
        return "redirect:/health";
    }
}
