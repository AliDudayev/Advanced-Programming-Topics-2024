package fact.it.achievements.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Duration;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AchievementResponse {
    private Long id;
    private String userCode;
    private Double fastestTime; // optional
    private Double longestDistance; // optional
    private Double maxWeightLifted; // optional
    private Double longestWorkoutDuration; // optional
    private Double mostCaloriesBurned; // optional
}