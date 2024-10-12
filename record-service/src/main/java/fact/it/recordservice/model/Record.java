package fact.it.recordservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Duration;

@Document(value = "record")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Record {
    private String id;
    private Double fastestTime; // optional
    private Double longestDistance; // optional
    private Double maxWeightLifted; // optional
    private Double longestWorkoutDuration; // optional
    private Double mostCaloriesBurned; // optional

}
