package fact.it.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutResponse {
    private String id;
    private String name;
    private LocalDate date;
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
