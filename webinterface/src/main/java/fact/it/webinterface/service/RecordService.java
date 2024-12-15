package fact.it.webinterface.service;

import org.springframework.stereotype.Service;

@Service
public class RecordService {

    private final ApiService apiService;
    private static final String BASE_PATH = "/record";

    public RecordService(ApiService apiService) {
        this.apiService = apiService;
    }

    // Get all records
    public Object getAllRecords() {
        return apiService.get(BASE_PATH + "/all", Object.class);  // GET /record/all
    }

    // Get a specific record by userCode
    public Object getRecord(String userCode) {
        return apiService.get(BASE_PATH + "?userCode=" + userCode, Object.class);  // GET /record?userCode=xxx
    }

    // Create a new record
    public Object createRecord(Object recordRequest) {
        return apiService.post(BASE_PATH, recordRequest, Object.class);
    }

    // Delete a record by userCode
    public void deleteRecord(String userCode) {
        apiService.delete(BASE_PATH + "?userCode=" + userCode);  // DELETE /record?userCode=xxx
    }
}
