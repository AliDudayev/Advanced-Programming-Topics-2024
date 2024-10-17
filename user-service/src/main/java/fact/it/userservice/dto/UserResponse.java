package fact.it.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String id;
    private String userCode;
    private String name;
    private double height;
    private double weight;
    private boolean male;
    private String email;
    private String fitnessGoals;
}