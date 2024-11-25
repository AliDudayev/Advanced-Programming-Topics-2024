package fact.it.achievements.repository;

import fact.it.achievements.model.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface AchievementRepository extends JpaRepository<Achievement, Long> {
    Achievement findByUserCode(String code);
}
