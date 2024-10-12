package fact.it.healthservice.repository;

import fact.it.healthservice.model.Health;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@Transactional
public interface HealthRepository extends JpaRepository<Health, Long> {
    Health findByWorkoutCode(String workoutCode);
}
