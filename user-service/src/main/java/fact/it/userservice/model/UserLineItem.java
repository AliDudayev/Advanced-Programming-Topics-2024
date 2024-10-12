package fact.it.userservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "RecordLineItem")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserLineItem {
    private String id;
    private String userCode;
    private Double fastestTime; // optional
    private Double longestDistance; // optional
    private Double maxWeightLifted; // optional
    private Double longestWorkoutDuration; // optional
    private Double mostCaloriesBurned; // optional
}
