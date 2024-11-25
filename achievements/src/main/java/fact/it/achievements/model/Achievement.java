package fact.it.achievements.model;

import jakarta.persistence.*;
import lombok.*;

@Entity  // Specifies that this class is an entity mapped to a database table
@Table(name = "achievement")  // Maps the class to the table named 'workout'
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Achievement {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)  // Auto-generates ID in MySQL
    private Long id;

    @Column(nullable = false)
    private String userCode;
    @Column(nullable = true)
    private Double fastestTime; // optional
    @Column(nullable = true)
    private Double longestDistance; // optional
    @Column(nullable = true)
    private Double maxWeightLifted; // optional
    @Column(nullable = true)
    private Double longestWorkoutDuration; // optional
    @Column(nullable = true)
    private Double mostCaloriesBurned; // optional
}