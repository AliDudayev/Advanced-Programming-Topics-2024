package fact.it.webinterface.controller;

import fact.it.webinterface.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RecordController {

    private final RecordService recordService;

    @Autowired
    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }

    @GetMapping("/records")
    public String getAllRecords(Model model) {
        model.addAttribute("records", recordService.getAllRecords());
        return "recordPage";
    }
}
