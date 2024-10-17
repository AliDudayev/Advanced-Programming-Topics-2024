package fact.it.userservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Indexed;

import java.util.List;

@Document(value = "user")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class User {
    @Id
    private String id;
    private String userCode;
    private String name;
    private int age;
    private double height;
    private double weight;
    private String email;
    private String phoneNr;
    private boolean male;
    private String fitnessGoals;

//    private List<UserLineItem> userLineItemList;
}
