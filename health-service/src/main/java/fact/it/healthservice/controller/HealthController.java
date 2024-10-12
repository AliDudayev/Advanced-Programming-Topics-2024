package fact.it.healthservice.controller;

import fact.it.healthservice.dto.HealthRequest;
import fact.it.healthservice.dto.HealthResponse;
import fact.it.healthservice.service.HealthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/health")
@RequiredArgsConstructor
public class HealthController {

    private final HealthService healthService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void createHealth
            (@RequestBody HealthRequest healthRequest) {
        healthService.createHealth(healthRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public HealthResponse getHealth
            (@RequestParam String workoutCode) {
        return healthService.getHealthByWorkoutCode(workoutCode);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<HealthResponse> getAllHealths() {
        return healthService.getAllHealths();
    }


    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void updateUser(@RequestParam String workoutCode, @RequestBody HealthRequest healthRequest) {
        healthService.updateHealth(workoutCode, healthRequest);
    }
}

