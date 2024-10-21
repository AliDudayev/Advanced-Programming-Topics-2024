package fact.it.workoutservice.repository;

import fact.it.workoutservice.model.Workout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    Workout findByWorkoutCode(String workoutCode);

    List<Workout> findByUserCode(String userCode);


}
