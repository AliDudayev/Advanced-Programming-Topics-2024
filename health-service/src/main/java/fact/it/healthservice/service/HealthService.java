package fact.it.healthservice.service;

import fact.it.healthservice.dto.HealthRequest;
import fact.it.healthservice.dto.HealthResponse;
import fact.it.healthservice.model.Health;
import fact.it.healthservice.repository.HealthRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HealthService {

    private final HealthRepository healthRepository;


    // add these to the builder below
    public void createHealth(HealthRequest healthRequest){
        if (healthRepository.findByWorkoutCode(healthRequest.getWorkoutCode()) == null) {
            Health health = Health.builder()
                    .workoutCode(healthRequest.getWorkoutCode())
                    .recoveryHeartRate(healthRequest.getRecoveryHeartRate())
                    .bloodPressure(healthRequest.getBloodPressure())
                    .caloriesBurned(healthRequest.getCaloriesBurned())
                    .oxygenSaturation(healthRequest.getOxygenSaturation())
                    .build();
            healthRepository.save(health);
        }
    }
    public List<HealthResponse> getAllHealths() {
        List<Health> health = healthRepository.findAll();

        return health.stream().map(this::mapToHealthResponse).toList();
    }

    @Transactional(readOnly = true)
    public HealthResponse getHealthByWorkoutCode(String workoutCode) {
         Health health = healthRepository.findByWorkoutCode(workoutCode);

        return mapToHealthResponse(health);
    }

    private HealthResponse mapToHealthResponse(Health health) {
        return HealthResponse.builder()
                .id(health.getId())
                .workoutCode(health.getWorkoutCode())
                .recoveryHeartRate(health.getRecoveryHeartRate())
                .bloodPressure(health.getBloodPressure())
                .caloriesBurned(health.getCaloriesBurned())
                .oxygenSaturation(health.getOxygenSaturation())
                .build();
    }

    public void updateHealth(String workoutCode, HealthRequest healthRequest) {
        if(healthRepository.findByWorkoutCode(workoutCode) == null){
            return;
        }
        Health health = healthRepository.findByWorkoutCode(workoutCode);
        health.setRecoveryHeartRate(healthRequest.getRecoveryHeartRate());
        health.setBloodPressure(healthRequest.getBloodPressure());
        health.setCaloriesBurned(healthRequest.getCaloriesBurned());
        health.setOxygenSaturation(healthRequest.getOxygenSaturation());
        healthRepository.save(health);
    }


}
