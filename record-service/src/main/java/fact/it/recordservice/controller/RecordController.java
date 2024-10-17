package fact.it.recordservice.controller;

import fact.it.recordservice.dto.RecordRequest;
import fact.it.recordservice.dto.RecordResponse;
import fact.it.recordservice.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/record")
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;

    // Het toevoegen van een record --> Klaar
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void createRecord
            (@RequestBody RecordRequest recordRequest) {
        recordService.createRecord(recordRequest);
    }

    // Het tonen van alle records --> Klaar
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<RecordResponse> getAllRecords() {
        return recordService.getAllRecords();
    }

    // Het updaten van een record --> Klaar
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void updateRecord(@RequestParam String userCode, @RequestBody RecordRequest recordRequest) {
        recordService.updateRecord(userCode, recordRequest);
    }


    // Het ophalen van een record op basis van een code --> Klaar
    // http://localhost:8082/api/record?code=user1
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public RecordResponse getRecordByCode
            (@RequestParam String userCode) {
        return recordService.getRecordByCode(userCode);
    }

    // Het verwijderen van een record --> Klaar
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteRecord(@RequestParam String userCode) {
        recordService.deleteRecord(userCode);
    }
}

