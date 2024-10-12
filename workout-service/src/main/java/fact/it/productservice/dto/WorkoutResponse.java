package fact.it.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutResponse {
    private String id;
    private String name;
    private Date date;
    private String duration;
    private String sets;
    private String reps;
    private String pauseBetweenReps;
    private String type;
    private String weight;
    private String distance;
    private String speed;
    private String description;
}