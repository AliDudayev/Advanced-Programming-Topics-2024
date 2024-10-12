package fact.it.recordservice.repository;

import fact.it.recordservice.model.Record;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RecordRepository extends MongoRepository<Record, String> {

}
