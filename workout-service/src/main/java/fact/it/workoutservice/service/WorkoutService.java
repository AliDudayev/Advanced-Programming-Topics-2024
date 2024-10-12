package fact.it.workoutservice.service;

import fact.it.workoutservice.dto.HealthResponse;
import fact.it.workoutservice.dto.WorkoutRequest;
import fact.it.workoutservice.dto.WorkoutResponse;
import fact.it.workoutservice.model.Workout;
import fact.it.workoutservice.repository.WorkoutRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkoutService {

    @Value("${healthservice.baseurl}")
    private String healthServiceBaseUrl;

    private final WorkoutRepository workoutRepository;
    private final WebClient webClient;

    public HealthResponse getHealthData(String workoutCode) {

                HealthResponse[] healthResponseArray = webClient.get()
                .uri("http://" + healthServiceBaseUrl + "/api/health",
                        uriBuilder -> uriBuilder.queryParam("workoutCode", workoutCode).build())
                .retrieve()
                .bodyToMono(HealthResponse[].class)
                .block();
                assert healthResponseArray != null;
                if (healthResponseArray.length == 0) {
                    return null;
                }
                return healthResponseArray[0];
    }


    public void createWorkout(WorkoutRequest workoutRequest){
        Workout workout = Workout.builder()
                .name(workoutRequest.getName())
                .userCode(workoutRequest.getUserCode())
                .workoutCode(workoutRequest.getWorkoutCode())
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

    public WorkoutResponse getWorkoutByWorkoutCode(String workoutCode) {
        Workout workout = workoutRepository.findByWorkoutCode(workoutCode);
        return mapToWorkoutResponse(workout);
    }

    private WorkoutResponse mapToWorkoutResponse(Workout workout) {
        return WorkoutResponse.builder()
                .id(String.valueOf(workout.getId()))
                .name(workout.getName())
                .userCode(workout.getUserCode())
                .workoutCode(workout.getWorkoutCode())
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
