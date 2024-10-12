package fact.it.productservice.service;

import fact.it.productservice.dto.WorkoutRequest;
import fact.it.productservice.dto.WorkoutResponse;
import fact.it.productservice.model.Workout;
import fact.it.productservice.repository.WorkoutRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    public void createWorkout(WorkoutRequest workoutRequest){
        Workout workout = Workout.builder()
                .name(workoutRequest.getName())
                .date(workoutRequest.getDate())
                .duration(workoutRequest.getDuration())
                .sets(workoutRequest.getSets())
                .reps(workoutRequest.getReps())
                .pauseBetweenReps(workoutRequest.getPauseBetweenReps())
                .type(workoutRequest.getType())
                .weight(workoutRequest.getWeight())
                .distance(workoutRequest.getDistance())
                .speed(workoutRequest.getSpeed())
                .description(workoutRequest.getDescription())
                .build();
        workoutRepository.save(workout);
    }
    public List<WorkoutResponse> getAllWorkouts() {
        List<Workout> workouts = workoutRepository.findAll();

        return workouts.stream().map(this::mapToWorkoutResponse).toList();
    }

    public List<WorkoutResponse> getAllWorkoutsByName(String name) {
        List<Workout> workouts = workoutRepository.findAllByName(name);

        return workouts.stream().map(this::mapToWorkoutResponse).toList();
    }

    private WorkoutResponse mapToWorkoutResponse(Workout workout) {
        return WorkoutResponse.builder()
                .id(String.valueOf(workout.getId()))
                .name(workout.getName())
                .date(workout.getDate())
                .duration(workout.getDuration())
                .sets(workout.getSets())
                .reps(workout.getReps())
                .pauseBetweenReps(workout.getPauseBetweenReps())
                .type(workout.getType())
                .weight(workout.getWeight())
                .distance(workout.getDistance())
                .speed(workout.getSpeed())
                .description(workout.getDescription())
                .build();
    }

}
