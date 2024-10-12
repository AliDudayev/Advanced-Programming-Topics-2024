package fact.it.workoutservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutRequest {
    private String name;
    private Date date;
    private String duration;
    private String sets;
    private String reps;

    private String userCode;
    private String workoutCode;
    private String pauseBetweenReps;
    private String type;
    private String weight;
    private String distance;
    private String speed;
    private String description;
}