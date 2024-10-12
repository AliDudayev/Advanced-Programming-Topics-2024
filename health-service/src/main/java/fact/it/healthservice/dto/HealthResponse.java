package fact.it.healthservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HealthResponse {
    private Long id;
    private String workoutCode;
    private String recoveryHeartRate;
    private String bloodPressure;
    private String caloriesBurned;
    private String oxygenSaturation;
}