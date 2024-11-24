package fact.it.recordservice.repository;

import fact.it.recordservice.model.Record;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface RecordRepository extends MongoRepository<Record, String> {
    Record findByUserCode(String code);
}
