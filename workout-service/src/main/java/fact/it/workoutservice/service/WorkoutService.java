package fact.it.workoutservice.service;


import fact.it.workoutservice.dto.*;
import fact.it.workoutservice.model.Workout;
import fact.it.workoutservice.repository.WorkoutRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

import java.util.List;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkoutService {

    @Value("${healthservice.baseurl}")
    private String healthServiceBaseUrl;

    @Value("${recordservice.baseurl}")
    private String recordServiceBaseUrl;

    @Value("${userservice.baseurl}")
    private String userServiceBaseUrl;

    private final WorkoutRepository workoutRepository;
    private final WebClient webClient;

//    public HealthResponse getHealthData(String workoutCode) {
//
//                HealthResponse[] healthResponseArray = webClient.get()
//                .uri("http://" + healthServiceBaseUrl + "/api/health",
//                        uriBuilder -> uriBuilder.queryParam("workoutCode", workoutCode).build())
//                .retrieve()
//                .bodyToMono(HealthResponse[].class)
//                .block();
//                assert healthResponseArray != null;
//                if (healthResponseArray.length == 0) {
//                    return null;
//                }
//                return healthResponseArray[0];
//    }

    public HealthResponse getHealthData(String workoutCode) {

        return webClient.get()
                .uri("http://" + healthServiceBaseUrl + "/api/health",
                        uriBuilder -> uriBuilder.queryParam("workoutCode", workoutCode).build())
                .retrieve()
                .bodyToMono(HealthResponse.class)
                .block();
    }


    public void createWorkout(WorkoutRequest workoutRequest){

        String workoutCode;

        workoutCode = workoutRequest.getWorkoutCode() + "-" + UUID.randomUUID().toString().substring(0, 5);

        Workout workout = Workout.builder()
                .name(workoutRequest.getName())
                .userCode(workoutRequest.getUserCode())
                .workoutCode(workoutCode)
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

        RecordResponse recordResponse = webClient.get()
                .uri( recordServiceBaseUrl + "/api/record",
                        uriBuilder -> uriBuilder.queryParam("userCode", workoutRequest.getUserCode()).build())
                .retrieve()
                .bodyToMono(RecordResponse.class)
                .block();

        Double fastestTime = recordResponse.getFastestTime();
        Double longestDistance = recordResponse.getLongestDistance();
        Double maxWeightLifted = recordResponse.getMaxWeightLifted();
        Double longestWorkoutDuration = recordResponse.getLongestWorkoutDuration();
        Double mostCaloriesBurned = recordResponse.getMostCaloriesBurned();

        double newFastestTime = 0;
        double newCaloriesBurned = 0;


        if (recordResponse != null) {
            if(workout.getDistance() != null && workout.getSpeed() != null)
            {

                newFastestTime = Double.parseDouble(workout.getDistance()) / Double.parseDouble(workout.getSpeed());
                if((newFastestTime < recordResponse.getFastestTime()) || (recordResponse.getFastestTime() == null))
                {
                    fastestTime = newFastestTime;
                }
            }

            if(workout.getDistance() != null && Double.parseDouble(workout.getDistance()) > recordResponse.getLongestDistance())
            {
                longestDistance = Double.parseDouble(workout.getDistance());
            }

            if(workout.getWeight() != null && Double.parseDouble(workout.getWeight()) > recordResponse.getMaxWeightLifted())
            {
                maxWeightLifted = Double.parseDouble(workout.getWeight());
            }

            if(workout.getDuration() != null && Double.parseDouble(workout.getDuration()) > recordResponse.getLongestWorkoutDuration())
            {
                longestWorkoutDuration = Double.parseDouble(workout.getDuration());
            }

            if(workout.getDistance() != null && workout.getSpeed() != null)
            {
                UserResponse userResponse = webClient.get()
                        .uri(userServiceBaseUrl + "/api/user",
                                uriBuilder -> uriBuilder.queryParam("userCode", workout.getUserCode()).build())
                        .retrieve()
                        .bodyToMono(UserResponse.class)
                        .block();

                double met = 0;
                if(Double.parseDouble(workout.getSpeed()) < 8.0)
                {
                    met = 6;
                }
                else if(Double.parseDouble(workout.getSpeed()) < 9.7)
                {
                    met = 8;
                }
                else if(Double.parseDouble(workout.getSpeed()) < 11.3)
                {
                    met = 10;
                }
                else if(Double.parseDouble(workout.getSpeed()) < 12.9)
                {
                    met = 11.5;
                }
                else
                {
                    met = 12.8;
                }

                newCaloriesBurned = met * userResponse.getWeight() * Double.parseDouble(workout.getDuration()) / 60;
                if(newCaloriesBurned > recordResponse.getMostCaloriesBurned())
                {
                    mostCaloriesBurned = newCaloriesBurned;
                }
            }

            RecordResponse newRecordResponse = RecordResponse.builder()
                    .userCode(workout.getUserCode())
                    .fastestTime(fastestTime)
                    .longestDistance(longestDistance)
                    .maxWeightLifted(maxWeightLifted)
                    .longestWorkoutDuration(longestWorkoutDuration)
                    .mostCaloriesBurned(mostCaloriesBurned)
                    .build();

            webClient.put()
                    .uri( recordServiceBaseUrl + "/api/record",
                            uriBuilder -> uriBuilder.queryParam("userCode", workout.getUserCode()).build())
                    .bodyValue(newRecordResponse)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();

        }
    }

    public List<WorkoutResponse> getAllWorkouts() {
        List<Workout> workouts = workoutRepository.findAll();

        return workouts.stream().map(this::mapToWorkoutResponse).toList();
    }

    public WorkoutResponse getWorkoutByWorkoutCode(String workoutCode) {
        Workout workout = workoutRepository.findByWorkoutCode(workoutCode);
        return mapToWorkoutResponse(workout);
    }

    @Transactional
    public List<WorkoutResponse> getWorkoutByUserCode(String userCode) {
        List<Workout> workouts = workoutRepository.findByUserCode(userCode);
        return workouts.stream().map(this::mapToWorkoutResponse).toList();
    }

    private WorkoutResponse mapToWorkoutResponse(Workout workout) {
        return WorkoutResponse.builder()
                .id(String.valueOf(workout.getId()))
                .name(workout.getName())
                .userCode(workout.getUserCode())
                .workoutCode(workout.getWorkoutCode())
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
