package fact.it.webinterface.controller;

import fact.it.webinterface.dto.RecordRequest;
import fact.it.webinterface.service.RecordService;
import fact.it.webinterface.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/records")
public class RecordController {

    private final RecordService recordService;
    private final TokenService tokenService;

    @Autowired
    public RecordController(RecordService recordService, TokenService tokenService) {
        this.recordService = recordService;
        this.tokenService = tokenService;
    }

    // Get all records
    @GetMapping
    public String getAllRecords(Model model) {
        model.addAttribute("records", recordService.getAllRecords());
        return "recordPage";
    }

    // Get records by user
    @GetMapping("/user")
    public String getRecordsByUser(@RequestParam("userCode") String userCode, Model model) {
        model.addAttribute("records", recordService.getRecord(userCode));
        return "recordPage";
    }

    // Add a new record
    @PostMapping("/add")
    public String addRecord(@RequestBody RecordRequest recordRequest, Model model) {
        recordService.createRecord(recordRequest);
        model.addAttribute("message", "Record created successfully!");
        return "redirect:/records";
    }

    // Update an existing record
    @PostMapping("/update")
    public String updateRecord(@RequestBody RecordRequest recordRequest, Model model) {
        recordService.updateRecord(recordRequest);
        model.addAttribute("message", "Record updated successfully!");
        return "redirect:/records";
    }

    // Delete a record
    @PostMapping("/delete/{userCode}")
    public String deleteRecord(@PathVariable String userCode, Model model) {
        String token = tokenService.getToken();
        if (token == null) {
            return "redirect:/error";
        }
        else {
            recordService.deleteRecord(userCode);
            model.addAttribute("message", "Record deleted successfully!");
            return "redirect:/records";
        }
    }
}
