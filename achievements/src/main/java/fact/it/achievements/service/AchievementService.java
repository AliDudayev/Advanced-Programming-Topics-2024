package fact.it.achievements.service;

import fact.it.achievements.dto.AchievementRequest;
import fact.it.achievements.dto.AchievementResponse;
import fact.it.achievements.model.Achievement;
import fact.it.achievements.repository.AchievementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AchievementService {
    private final AchievementRepository achievementRepository;

    // Create a new record --> Klaar
    public void createRecord(AchievementRequest achievementRequest){
        if (achievementRepository.findByUserCode(achievementRequest.getUserCode()) == null) {
            Achievement record = Achievement.builder()
                    .userCode(achievementRequest.getUserCode())
                    .fastestTime(achievementRequest.getFastestTime())
                    .longestDistance(achievementRequest.getLongestDistance())
                    .maxWeightLifted(achievementRequest.getMaxWeightLifted())
                    .longestWorkoutDuration(achievementRequest.getLongestWorkoutDuration())
                    .mostCaloriesBurned(achievementRequest.getMostCaloriesBurned())
                    .build();
            achievementRepository.save(record);
        }
    }

    // Create a new record with userCode (from the user) --> Klaar
    @Transactional(readOnly = true)
    public void createRecord(String userCode, AchievementRequest recordRequest){
        if(achievementRepository.findByUserCode(userCode) == null) {
            Achievement record = Achievement.builder()
                    .userCode(userCode)
                    .fastestTime(recordRequest.getFastestTime())
                    .longestDistance(recordRequest.getLongestDistance())
                    .maxWeightLifted(recordRequest.getMaxWeightLifted())
                    .longestWorkoutDuration(recordRequest.getLongestWorkoutDuration())
                    .mostCaloriesBurned(recordRequest.getMostCaloriesBurned())
                    .build();
            achievementRepository.save(record);
        }
    }


    // Get all records --> Klaar
    @Transactional(readOnly = true)
    public List<AchievementResponse> getAllRecords() {
        List<Achievement> records = achievementRepository.findAll();

        return records.stream().map(this::mapToRecordResponse).toList();
    }

    // Save the updated record --> Klaar
    @Transactional(readOnly = true)
    public void updateRecord(String userCode, AchievementRequest recordRequest) {
        Achievement record = achievementRepository.findByUserCode(userCode);

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

        achievementRepository.save(record);
    }

    // Get record by code --> Klaar
    @Transactional(readOnly = true)
    public AchievementResponse getRecordByCode(String code) {
        Achievement record = achievementRepository.findByUserCode(code);
        return record != null ? mapToRecordResponse(record) : null;
    }

    // Delete record by userCode --> Klaar
    @Transactional(readOnly = true)
    public void deleteRecord(String userCode) {
        if (achievementRepository.findByUserCode(userCode) != null) {
            Achievement record = achievementRepository.findByUserCode(userCode);
            achievementRepository.delete(record);
        }
    }

    // Map record to record response --> Klaar
    private AchievementResponse mapToRecordResponse(Achievement record) {
        return AchievementResponse.builder()
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
