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

    @Autowired
    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }

    // Get all records
    @GetMapping
    public String getAllRecords(Model model) {
        model.addAttribute("records", recordService.getAllRecords());
        return "recordPage";
    }
}
