package fact.it.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
//    private String id;
    private String name;
    private String userCode;
    private Integer age;
    private double height;
    private double weight;
    private String phoneNr;
    private boolean male;
    private String email;
    private String fitnessGoals;
}