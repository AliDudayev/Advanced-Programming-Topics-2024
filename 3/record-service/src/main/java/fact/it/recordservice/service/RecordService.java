package fact.it.recordservice.service;

import fact.it.recordservice.dto.RecordRequest;
import fact.it.recordservice.dto.RecordResponse;
import fact.it.recordservice.model.Record;
import fact.it.recordservice.repository.RecordRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecordService {

    private final RecordRepository recordRepository;

    // Create a new record --> Klaar
    public void createRecord(RecordRequest recordRequest){
        if (recordRepository.findByUserCode(recordRequest.getUserCode()) == null) {
            Record record = Record.builder()
                    .userCode(recordRequest.getUserCode())
                    .fastestTime(recordRequest.getFastestTime())
                    .longestDistance(recordRequest.getLongestDistance())
                    .maxWeightLifted(recordRequest.getMaxWeightLifted())
                    .longestWorkoutDuration(recordRequest.getLongestWorkoutDuration())
                    .mostCaloriesBurned(recordRequest.getMostCaloriesBurned())
                    .build();
            recordRepository.save(record);
        }
    }

    // Create a new record with userCode (from the user) --> Klaar
    @Transactional(readOnly = true)
    public void createRecord(String userCode, RecordRequest recordRequest){
        if(recordRepository.findByUserCode(userCode) == null) {
            Record record = Record.builder()
                    .userCode(userCode)
                    .fastestTime(recordRequest.getFastestTime())
                    .longestDistance(recordRequest.getLongestDistance())
                    .maxWeightLifted(recordRequest.getMaxWeightLifted())
                    .longestWorkoutDuration(recordRequest.getLongestWorkoutDuration())
                    .mostCaloriesBurned(recordRequest.getMostCaloriesBurned())
                    .build();
            recordRepository.save(record);
        }
    }


    // Get all records --> Klaar
    @Transactional(readOnly = true)
    public List<RecordResponse> getAllRecords() {
        List<Record> records = recordRepository.findAll();

        return records.stream().map(this::mapToRecordResponse).toList();
    }

    // Save the updated record --> Klaar
    @Transactional(readOnly = true)
    public void updateRecord(String userCode, RecordRequest recordRequest) {
        Record record = recordRepository.findByUserCode(userCode);

        if (record == null) {
            // return a message to postman that the record does not exist
            this.createRecord(userCode, recordRequest);

            return;
        }

        record.setFastestTime(recordRequest.getFastestTime());
        record.setLongestDistance(recordRequest.getLongestDistance());
        record.setMaxWeightLifted(recordRequest.getMaxWeightLifted());
        record.setLongestWorkoutDuration(recordRequest.getLongestWorkoutDuration());
        record.setMostCaloriesBurned(recordRequest.getMostCaloriesBurned());

        recordRepository.save(record);
    }

    // Get record by code --> Klaar
    @Transactional(readOnly = true)
    public RecordResponse getRecordByCode(String code) {
        Record record = recordRepository.findByUserCode(code);
        return record != null ? mapToRecordResponse(record) : null;
    }

    // Delete record by userCode --> Klaar
    @Transactional(readOnly = true)
    public void deleteRecord(String userCode) {
        if (recordRepository.findByUserCode(userCode) != null) {
            Record record = recordRepository.findByUserCode(userCode);
            recordRepository.delete(record);
        }
    }

    // Map record to record response --> Klaar
    private RecordResponse mapToRecordResponse(Record record) {
        return RecordResponse.builder()
                .id(record.getId())
                .userCode(record.getUserCode())
                .maxWeightLifted(record.getMaxWeightLifted())
                .longestDistance(record.getLongestDistance())
                .fastestTime(record.getFastestTime())
                .longestWorkoutDuration(record.getLongestWorkoutDuration())
                .mostCaloriesBurned(record.getMostCaloriesBurned())
                .build();
    }

}
