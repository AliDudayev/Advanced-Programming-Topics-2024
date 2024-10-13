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

    public void createRecord(RecordRequest recordRequest){
        Record record = Record.builder()
                .fastestTime(recordRequest.getFastestTime())
                .longestDistance(recordRequest.getLongestDistance())
                .maxWeightLifted(recordRequest.getMaxWeightLifted())
                .longestWorkoutDuration(recordRequest.getLongestWorkoutDuration())
                .mostCaloriesBurned(recordRequest.getMostCaloriesBurned())
                .build();

        recordRepository.save(record);
    }

    public List<RecordResponse> getAllRecords() {
        List<Record> records = recordRepository.findAll();

        return records.stream().map(this::mapToRecordResponse).toList();
    }

//    @Transactional(readOnly = true)
//    // get record by id
//    public RecordResponse getRecordByCode(String code) {
//        Record record = recordRepository.findByUserCode(code);
//        return mapToRecordResponse(record);
//    }
    @Transactional(readOnly = true)
    // get record by id
    public RecordResponse getRecordByCode(String code) {
        Record record = recordRepository.findByUserCode(code);

        return record != null ? mapToRecordResponse(record) : null;
    }

    // Save the updated record
    public void updateRecord(String id, RecordRequest recordRequest) {
        Record record = recordRepository.findById(id).orElseThrow();

        record.setFastestTime(recordRequest.getFastestTime());
        record.setLongestDistance(recordRequest.getLongestDistance());
        record.setMaxWeightLifted(recordRequest.getMaxWeightLifted());
        record.setLongestWorkoutDuration(recordRequest.getLongestWorkoutDuration());
        record.setMostCaloriesBurned(recordRequest.getMostCaloriesBurned());

        recordRepository.save(record);
    }

    // Delete record by id
    public void deleteRecord(String id) {
        recordRepository.deleteById(id);
    }

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
