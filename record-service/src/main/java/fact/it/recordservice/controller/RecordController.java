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

    @PostMapping // Het toevoegen van een record
    @ResponseStatus(HttpStatus.OK)
    public void createRecord
            (@RequestBody RecordRequest recordRequest) {
        recordService.createRecord(recordRequest);
    }

    @GetMapping // Het ophalen van een record op basis van een id
    @ResponseStatus(HttpStatus.OK)
    public RecordResponse getRecordById
            (@RequestParam String id) {
        return recordService.getRecordById(id);
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
