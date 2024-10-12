package fact.it.recordservice.repository;

import fact.it.recordservice.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RecordRepository extends MongoRepository<Product, String> {
    List<Product> findBySkuCodeIn(List<String> skuCode);
}
