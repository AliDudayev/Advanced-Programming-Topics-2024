package fact.it.workoutservice.controller;

import fact.it.workoutservice.dto.WorkoutRequest;
import fact.it.workoutservice.dto.WorkoutResponse;
import fact.it.workoutservice.service.WorkoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workout")
@RequiredArgsConstructor
public class WorkoutController {

    private final WorkoutService workoutService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void createWorkout
            (@RequestBody WorkoutRequest workoutRequest) {
        workoutService.createWorkout(workoutRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<WorkoutResponse> getWorkout
            (@RequestParam String name) {
        return workoutService.getAllWorkoutsByName(name);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<WorkoutResponse> getAllWorkouts() {
        return workoutService.getAllWorkouts();
    }
}

