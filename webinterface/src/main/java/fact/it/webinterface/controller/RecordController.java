package fact.it.webinterface.controller;

import fact.it.webinterface.dto.RecordRequest;
import fact.it.webinterface.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/records/user")
    public String getRecordsByUser(@RequestParam("userCode") String userCode, Model model) {
        model.addAttribute("records", recordService.getRecord(userCode));
        return "recordPage";
    }

    @PostMapping("/addRecord")
    public String addRecord(@RequestParam("userCode") String userCode,
                            @RequestParam("fastestTime") String fastestTime,
                            @RequestParam("longestDistance") String longestDistance,
                            @RequestParam("maxWeightLifted") String maxWeightLifted,
                            @RequestParam("longestWorkoutDuration") String longestWorkoutDuration,
                            @RequestParam("mostCaloriesBurned") String mostCaloriesBurned,
                            Model model) {
        RecordRequest recordRequest = new RecordRequest(
                userCode, fastestTime, longestDistance, maxWeightLifted, longestWorkoutDuration, mostCaloriesBurned
        );
        recordService.createRecord(recordRequest);
        model.addAttribute("message", "Record created successfully!");
        return "redirect:/records";
    }
}
