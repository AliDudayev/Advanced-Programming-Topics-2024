package fact.it.recordservice;
import fact.it.recordservice.dto.RecordResponse;
import fact.it.recordservice.model.Record;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import fact.it.recordservice.dto.RecordRequest;
import fact.it.recordservice.repository.RecordRepository;
import fact.it.recordservice.service.RecordService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
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

    @Test
    @Order(1)
        public void createRecord_WithValidRequest_RecordIsSaved() {
        // I want to make a unittest here that creates a record and checks if it is saved in the database
        RecordRequest recordRequest = RecordRequest.builder()
                .userCode("TestRecord126")
                .fastestTime(10.0)
                .longestDistance(100.0)
                .maxWeightLifted(50.0)
                .longestWorkoutDuration(60.0)
                .mostCaloriesBurned(500.0)
                .build();

        recordService.createRecord(recordRequest);

        // To test that there are no errors when creating a record that already exists
        recordService.createRecord(recordRequest);

        RecordResponse record = recordService.getRecordByCode("TestRecord126");

        assertEquals("TestRecord126", record.getUserCode());
        assertEquals(10.0, record.getFastestTime());
        assertEquals(100.0, record.getLongestDistance());
        assertEquals(50.0, record.getMaxWeightLifted());
        assertEquals(60.0, record.getLongestWorkoutDuration());
        assertEquals(500.0, record.getMostCaloriesBurned());
    }

    @Test
    @Order(2)
    public void createRecord_WithExistingUserCode_RecordIsNotSaved() {
        // I want to make a unittest here that creates a record with an existing userCode and checks if it is not saved in the database
        RecordRequest recordRequest = RecordRequest.builder()
                .userCode("TestRecord126")
                .fastestTime(99.0)
                .longestDistance(99.0)
                .maxWeightLifted(99.0)
                .longestWorkoutDuration(99.0)
                .mostCaloriesBurned(99.0)
                .build();

        recordService.createRecord(recordRequest);

        RecordResponse record = recordService.getRecordByCode("TestRecord126");

        assertEquals("TestRecord126", record.getUserCode());
        assertEquals(10.0, record.getFastestTime());
        assertEquals(100.0, record.getLongestDistance());
        assertEquals(50.0, record.getMaxWeightLifted());
        assertEquals(60.0, record.getLongestWorkoutDuration());
        assertEquals(500.0, record.getMostCaloriesBurned());
    }

    // Create a record with a usercode in the request that does not exist
    @Test
    @Order(3)
    public void createRecord_WithUserCodeInTheInUrl_RecordIsSaved() {
        // I want to make a unittest here that creates a record with a non-existing userCode and checks if it is not saved in the database
        RecordRequest recordRequest = RecordRequest.builder()
                .userCode("Test")
                .fastestTime(10.0)
                .longestDistance(100.0)
                .maxWeightLifted(50.0)
                .longestWorkoutDuration(60.0)
                .mostCaloriesBurned(500.0)
                .build();

        recordService.createRecord("TestRecord127", recordRequest);

        RecordResponse record = recordService.getRecordByCode("TestRecord127");

        assertEquals("TestRecord127", record.getUserCode());
        assertEquals(10.0, record.getFastestTime());
        assertEquals(100.0, record.getLongestDistance());
        assertEquals(50.0, record.getMaxWeightLifted());
        assertEquals(60.0, record.getLongestWorkoutDuration());
        assertEquals(500.0, record.getMostCaloriesBurned());

    }

    @Test
    @Order(4)
    public void createRecord_WithExistingUserCodeInTheInUrl_RecordIsSaved() {
        // I want to make a unittest here that creates a record with a non-existing userCode and checks if it is not saved in the database
        RecordRequest recordRequest = RecordRequest.builder()
                .userCode("Test")
                .fastestTime(0.0)
                .longestDistance(0.0)
                .maxWeightLifted(0.0)
                .longestWorkoutDuration(0.0)
                .mostCaloriesBurned(0.0)
                .build();

        recordService.createRecord("TestRecord127", recordRequest);

        RecordResponse record = recordService.getRecordByCode("TestRecord127");

        assertEquals("TestRecord127", record.getUserCode());
        assertEquals(10.0, record.getFastestTime());
        assertEquals(100.0, record.getLongestDistance());
        assertEquals(50.0, record.getMaxWeightLifted());
        assertEquals(60.0, record.getLongestWorkoutDuration());
        assertEquals(500.0, record.getMostCaloriesBurned());

    }

    @Test
    @Order(5)
    public void updateRecord_WithValidRequest_RecordIsUpdated() {
        // I want to make a unittest here that updates a record and checks if it is updated in the database
        RecordRequest recordRequest = RecordRequest.builder()
                .fastestTime(20.0)
                .longestDistance(200.0)
                .maxWeightLifted(100.0)
                .longestWorkoutDuration(120.0)
                .mostCaloriesBurned(1000.0)
                .build();

        recordService.updateRecord("TestRecord126", recordRequest);

        recordService.updateRecord("Test", recordRequest);

        RecordResponse record = recordService.getRecordByCode("TestRecord126");

        assertEquals("TestRecord126", record.getUserCode());
        assertEquals(20.0, record.getFastestTime());
        assertEquals(200.0, record.getLongestDistance());
        assertEquals(100.0, record.getMaxWeightLifted());
        assertEquals(120.0, record.getLongestWorkoutDuration());
        assertEquals(1000.0, record.getMostCaloriesBurned());
    }

    @Test
    @Order(6)
    public void getAllRecords_WithRecords_ReturnsRecords() {
        assertEquals("TestRecord127", recordService.getAllRecords().get(recordService.getAllRecords().size() - 1).getUserCode());
        assertEquals("TestRecord126", recordService.getAllRecords().get(recordService.getAllRecords().size() - 2).getUserCode());
    }

    @Test
    @Order(7)
    public void deleteRecord_WithExistingRecord_RemovesRecordSuccessfully() {
        recordService.deleteRecord("TestRecord126");
        recordService.deleteRecord("FakeRecord");
        assertNull(recordService.getRecordByCode("TestRecord126"));
    }

    @Test
    @Order(8)
    public void cleanupTestRecords_RemovesTestRecords() {
        recordService.deleteRecord("TestRecord127");
    }
}
