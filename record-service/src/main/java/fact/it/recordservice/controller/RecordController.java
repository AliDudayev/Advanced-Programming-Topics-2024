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

    @PostMapping // Het toevoegen van een record --> Klaar
    @ResponseStatus(HttpStatus.OK)
    public void createRecord
            (@RequestBody RecordRequest recordRequest) {
        recordService.createRecord(recordRequest);
    }

    // http://localhost:8082/api/record?code=user1
    @GetMapping // Het ophalen van een record op basis van een code
    @ResponseStatus(HttpStatus.OK)
    public RecordResponse getRecordByCode
            (@RequestParam String code) {
        //return recordService.getRecordByCode(code);
        return recordService.getRecordByCode(code);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<RecordResponse> getAllRecords() {
        return recordService.getAllRecords();
    }

    @PutMapping// Find the record by id and update it
    @ResponseStatus(HttpStatus.OK)
    public void updateRecord(@RequestParam String id, @RequestBody RecordRequest recordRequest) {
        recordService.updateRecord(id, recordRequest);
    }

    @DeleteMapping // Het verwijderen van een record op basis van een id
    @ResponseStatus(HttpStatus.OK)
    public void deleteRecord(@RequestParam String id) {
        recordService.deleteRecord(id);
    }
}

