package fact.it.recordservice;

import fact.it.recordservice.repository.RecordRepository;
import fact.it.recordservice.service.RecordService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RecordServiceApplicationTests {

    @Autowired
    private RecordService recordService;
    @Autowired
    private RecordRepository recordRepository;

    @Test
    void contextLoads() {
    }

}
