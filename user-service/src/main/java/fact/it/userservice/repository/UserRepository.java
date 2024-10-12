package fact.it.userservice.repository;

import fact.it.userservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Order, Long> {
}
