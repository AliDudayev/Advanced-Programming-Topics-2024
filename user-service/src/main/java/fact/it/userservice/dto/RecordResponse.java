package fact.it.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecordResponse {
    private String id;
    private String userCode;
    private Double fastestTime; // optional
    private Double longestDistance; // optional
    private Double maxWeightLifted; // optional
    private Double longestWorkoutDuration; // optional
    private Double mostCaloriesBurned; // optional

}
