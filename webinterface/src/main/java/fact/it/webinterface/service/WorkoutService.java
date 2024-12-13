package fact.it.webinterface.service;

import org.springframework.stereotype.Service;

@Service
public class WorkoutService {

    private final ApiService apiService;
    private static final String BASE_PATH = "/workout";

    public WorkoutService(ApiService apiService) {
        this.apiService = apiService;
    }

    // Get all workouts
    public Object getAllWorkouts() {
        return apiService.get(BASE_PATH + "/all", Object.class);  // GET /workout/all
    }

    // Get a specific workout by workoutCode
    public Object getWorkout(String workoutCode) {
        return apiService.get(BASE_PATH + "?workoutCode=" + workoutCode, Object.class);  // GET /workout?workoutCode=xxx
    }

    // Create a new workout
    public Object createWorkout(Object workoutRequest) {
        return apiService.post(BASE_PATH, workoutRequest, Object.class);
    }

    // Update a workout
    public void updateWorkout(Object workoutRequest) {
        apiService.put(BASE_PATH, workoutRequest, Object.class);
    }

    // Delete a workout by workoutCode
    public void deleteWorkout(String workoutCode) {
        apiService.delete(BASE_PATH + "?workoutCode=" + workoutCode);  // DELETE /workout?workoutCode=xxx
    }
}
