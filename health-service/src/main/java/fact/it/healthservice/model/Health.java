package fact.it.healthservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity  // Specifies that this class is an entity mapped to a database table
@Table(name = "health")  // Maps the class to the table named 'workout'
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Health {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)  // Auto-generates ID in MySQL
    private Long id;

    @Column(nullable = true)
    private String recoveryHeartRate;

    @Column(nullable = true)
    private String bloodPressure;

    // link to workouts
    @Column(nullable = false)
    private String workoutCode;

    @Column(nullable = true)
    private String caloriesBurned;

    @Column(nullable = true)
    private String oxygenSaturation;
}
