package fact.it.workoutservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Entity  // Specifies that this class is an entity mapped to a database table
@Table(name = "workout")  // Maps the class to the table named 'workout'
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Workout {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)  // Auto-generates ID in MySQL
    private Long id;

    @Column(nullable = false)  // 'name' is a required field
    private String name;

    @Column(nullable = true)  // Optional 'date' field
    private LocalDate date;

    @Column(nullable = true)  // Optional 'duration' field
    private String duration;

    @Column(nullable = true)  // Optional 'sets' field
    private String sets;

    @Column(nullable = true)  // Optional 'reps' field
    private String reps;


    @Column(nullable = false)
    private String workoutCode;

    @Column(nullable = false)
    private String userCode;

    @Column(nullable = true)  // Optional 'pauseBetweenReps' field
    private String pauseBetweenReps;

    @Column(nullable = true)  // Optional 'type' field
    private String type;

    @Column(nullable = true)  // Optional 'weight' field
    private String weight;

    @Column(nullable = true)  // Optional 'distance' field
    private String distance;

    @Column(nullable = true)  // Optional 'speed' field
    private String speed;

    @Column(nullable = true)  // Optional 'description' field
    private String description;

}
