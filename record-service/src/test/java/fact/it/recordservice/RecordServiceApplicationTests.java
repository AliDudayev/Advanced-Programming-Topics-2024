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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


//@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RecordServiceApplicationTests {

    @InjectMocks
    private RecordService recordService;

    @Mock
    private RecordRepository recordRepository;

    @Test
    @Order(1)
    void createRecord_NewRecord_SavesSuccessfully() {
        // Arrange
        RecordRequest request = RecordRequest.builder()
                .userCode("user123")
                .fastestTime(12.5)
                .longestDistance(100.0)
                .maxWeightLifted(50.0)
                .longestWorkoutDuration(60.0)
                .mostCaloriesBurned(500.0)
                .build();

        when(recordRepository.findByUserCode("user123")).thenReturn(null);

        // Act
        recordService.createRecord(request);

        // Assert
        verify(recordRepository, times(1)).save(any(Record.class));
    }

    @Test
    @Order(2)
    void createRecord_NewRecordWithUserCodeInUrl_SavesSuccessfully() {
        // Arrange
        RecordRequest request = RecordRequest.builder()
                .fastestTime(12.5)
                .longestDistance(100.0)
                .maxWeightLifted(50.0)
                .longestWorkoutDuration(60.0)
                .mostCaloriesBurned(500.0)
                .build();

        when(recordRepository.findByUserCode("user123")).thenReturn(null);

        // Act
        recordService.createRecord("user123", request);

        // Assert
        verify(recordRepository, times(1)).save(any(Record.class));
    }

    @Test
    @Order(3)
    void createRecord_ExistingRecordWithUserCodeInUrl_DoesNotSaveAgain() {
        // Arrange
        Record existingRecord = Record.builder().userCode("user123").build();
        RecordRequest request = RecordRequest.builder()
                .fastestTime(12.5)
                .longestDistance(100.0)
                .maxWeightLifted(50.0)
                .longestWorkoutDuration(60.0)
                .mostCaloriesBurned(500.0)
                .build();

        when(recordRepository.findByUserCode("user123")).thenReturn(existingRecord);

        // Act
        recordService.createRecord("user123", request);

        // Assert
        verify(recordRepository, never()).save(any(Record.class));
    }

    @Test
    @Order(4)
    void createRecord_ExistingRecord_DoesNotSaveAgain() {
        // Arrange
        Record existingRecord = Record.builder().userCode("user123").build();
        RecordRequest request = RecordRequest.builder()
                .userCode("user123")
                .build();

        when(recordRepository.findByUserCode("user123")).thenReturn(existingRecord);

        // Act
        recordService.createRecord(request);

        // Assert
        verify(recordRepository, never()).save(any(Record.class));
    }

    @Test
    @Order(5)
    void getAllRecords_NewRecords_ReturnsRecordList() {
        // Arrange
        Record record1 = Record.builder().userCode("user1")
                .fastestTime(10.0)
                .longestDistance(50.0)
                .maxWeightLifted(50.0)
                .longestWorkoutDuration(80.0)
                .mostCaloriesBurned(1000.0)
                .build();
        Record record2 = Record.builder().userCode("user2")
                .fastestTime(20.0)
                .longestDistance(100.0)
                .maxWeightLifted(100.0)
                .longestWorkoutDuration(60.0)
                .mostCaloriesBurned(500.0)
                .build();

        when(recordRepository.findAll()).thenReturn(Arrays.asList(record1, record2));

        // Act
        List<RecordResponse> result = recordService.getAllRecords();

        // Assert
        assertEquals(2, result.size());
        assertEquals("user1", result.get(0).getUserCode());
        assertEquals(10.0, result.get(0).getFastestTime());
        assertEquals(50.0, result.get(0).getLongestDistance());
        assertEquals(50.0, result.get(0).getMaxWeightLifted());
        assertEquals(80.0, result.get(0).getLongestWorkoutDuration());
        assertEquals(1000.0, result.get(0).getMostCaloriesBurned());

        assertEquals("user2", result.get(1).getUserCode());
        assertEquals(20.0, result.get(1).getFastestTime());
        assertEquals(100.0, result.get(1).getLongestDistance());
        assertEquals(100.0, result.get(1).getMaxWeightLifted());
        assertEquals(60.0, result.get(1).getLongestWorkoutDuration());
        assertEquals(500.0, result.get(1).getMostCaloriesBurned());
    }

    @Test
    @Order(6)
    void getRecordByCode_ExistingCode_ReturnsRecordResponse() {
        // Arrange
        Record record = Record.builder()
                .userCode("user123")
                .fastestTime(10.0)
                .longestDistance(50.0)
                .maxWeightLifted(50.0)
                .longestWorkoutDuration(80.0)
                .mostCaloriesBurned(1000.0)
                .build();

        when(recordRepository.findByUserCode("user123")).thenReturn(record);

        // Act
        RecordResponse result = recordService.getRecordByCode("user123");

        // Assert
        assertNotNull(result);
        assertEquals("user123", result.getUserCode());
        assertEquals(10.0, result.getFastestTime());
        assertEquals(50.0, result.getLongestDistance());
        assertEquals(50.0, result.getMaxWeightLifted());
        assertEquals(80.0, result.getLongestWorkoutDuration());
        assertEquals(1000.0, result.getMostCaloriesBurned());
    }

    @Test
    @Order(7)
    void getRecordByCode_NonExistingCode_ReturnsNull() {
        // Arrange
        when(recordRepository.findByUserCode("user123")).thenReturn(null);

        // Act
        RecordResponse result = recordService.getRecordByCode("user123");

        // Assert
        assertNull(result);
    }

    @Test
    @Order(8)
    void updateRecord_ExistingRecord_UpdatesSuccessfully() {
        // Arrange
        Record existingRecord = Record.builder()
                .userCode("user123")
                .fastestTime(1.0)
                .longestDistance(1.0)
                .maxWeightLifted(1.0)
                .longestWorkoutDuration(1.0)
                .mostCaloriesBurned(1.0)
                .build();
        RecordRequest request = RecordRequest.builder()
                .fastestTime(100.0)
                .longestDistance(100.0)
                .maxWeightLifted(100.0)
                .longestWorkoutDuration(100.0)
                .mostCaloriesBurned(100.0)
                .build();
        when(recordRepository.findByUserCode("user123")).thenReturn(existingRecord);

        // Act
        recordService.updateRecord("user123", request);

        // Assert
        verify(recordRepository, times(1)).save(existingRecord);
        assertEquals(100.0, existingRecord.getFastestTime());
        assertEquals(100.0, existingRecord.getLongestDistance());
        assertEquals(100.0, existingRecord.getMaxWeightLifted());
        assertEquals(100.0, existingRecord.getLongestWorkoutDuration());
        assertEquals(100.0, existingRecord.getMostCaloriesBurned());
    }

    @Test
    @Order(9)
    void updateRecord_NonExistingRecord_CreatesNewRecord() {
        // Arrange
        RecordRequest request = RecordRequest.builder()
                .userCode("user123")
                .fastestTime(12.5)
                .build();

        when(recordRepository.findByUserCode("user123")).thenReturn(null);

        // Act
        recordService.updateRecord("user123", request);

        // Assert
        verify(recordRepository, times(1)).save(any(Record.class));
    }

    @Test
    @Order(10)
    void deleteRecord_ExistingRecord_DeletesSuccessfully() {
        // Arrange
        Record existingRecord = Record.builder().userCode("user123").build();

        when(recordRepository.findByUserCode("user123")).thenReturn(existingRecord);

        // Act
        recordService.deleteRecord("user123");

        // Assert
        verify(recordRepository, times(1)).delete(existingRecord);
    }

    @Test
    @Order(11)
    void deleteRecord_NonExistingRecord_NoActionTaken() {
        // Arrange
        when(recordRepository.findByUserCode("user123")).thenReturn(null);

        // Act
        recordService.deleteRecord("user123");

        // Assert
        verify(recordRepository, never()).delete(any(Record.class));
    }
}
