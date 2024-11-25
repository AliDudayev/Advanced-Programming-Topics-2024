package fact.it.achievements.controller;

import fact.it.achievements.dto.AchievementRequest;
import fact.it.achievements.dto.AchievementResponse;
import fact.it.achievements.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/record")
@RequiredArgsConstructor
public class AchievementController {

    private final AchievementService achievementService;

    // Het toevoegen van een record --> Klaar
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void createRecord
    (@RequestParam(required = false) String userCode, @RequestBody AchievementRequest achievementRequest) {
        if(userCode == null) {
            achievementService.createRecord(achievementRequest);
        }
        else {
            achievementService.createRecord(userCode, achievementRequest);
        }
    }

    // Het tonen van alle records --> Klaar
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<AchievementResponse> getAllRecords() {
        return achievementService.getAllRecords();
    }

    // Het updaten van een record --> Klaar
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void updateRecord(@RequestParam String userCode, @RequestBody AchievementRequest recordRequest) {
        achievementService.updateRecord(userCode, recordRequest);
    }


    // Het ophalen van een record op basis van een code --> Klaar
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public AchievementResponse getRecordByCode
    (@RequestParam String userCode) {
        return achievementService.getRecordByCode(userCode);
    }

    // Het verwijderen van een record --> Klaar
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteRecord(@RequestParam String userCode) {
        achievementService.deleteRecord(userCode);
    }
}