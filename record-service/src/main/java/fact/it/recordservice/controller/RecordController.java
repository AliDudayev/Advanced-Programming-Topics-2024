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
 
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void createRecord
    (@RequestParam(required = false) String userCode, @RequestBody RecordRequest recordRequest) {
        if(userCode == null) {
            recordService.createRecord(recordRequest);
        }
        else {
            recordService.createRecord(userCode, recordRequest);
        }
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<RecordResponse> getAllRecords() {
        return recordService.getAllRecords();
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void updateRecord(@RequestParam String userCode, @RequestBody RecordRequest recordRequest) {
        recordService.updateRecord(userCode, recordRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public RecordResponse getRecordByCode
    (@RequestParam String userCode) {
        return recordService.getRecordByCode(userCode);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteRecord(@RequestParam String userCode) {
        recordService.deleteRecord(userCode);
    }
}

