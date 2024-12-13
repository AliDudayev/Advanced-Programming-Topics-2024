package fact.it.webinterface.service;

import org.springframework.stereotype.Service;

@Service
public class HealthService {

    private final ApiService apiService;
    private static final String BASE_PATH = "/health";

    public HealthService(ApiService apiService) {
        this.apiService = apiService;
    }

    // Get all health records
    public Object getAllHealthRecords() {
        return apiService.get(BASE_PATH + "/all", Object.class);  // GET /health/all
    }

    // Get a specific health record by workoutcode
    public Object getHealthRecord(String workoutcode) {
        return apiService.get(BASE_PATH + "?workoutcode=" + workoutcode, Object.class);  // GET /health?workoutcode=xxx
    }

    // Create a new health record
    public Object createHealthRecord(Object healthRequest) {
        return apiService.post(BASE_PATH, healthRequest, Object.class);
    }

    // Update a health record
    public void updateHealthRecord(Object healthRequest) {
        apiService.put(BASE_PATH, healthRequest, Object.class);
    }
}
