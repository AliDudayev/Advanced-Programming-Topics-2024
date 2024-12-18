package fact.it.webinterface.service;

import fact.it.webinterface.dto.WorkoutRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WorkoutService {

    private final RestTemplate restTemplate;
    private final TokenService tokenService; // Needed only for delete
    private static final String BASE_PATH = "/workout";

    @Value("${api.gateway.url}")
    private String apiGatewayUrl;

    public WorkoutService(RestTemplate restTemplate, TokenService tokenService) {
        this.restTemplate = restTemplate;
        this.tokenService = tokenService;
    }

    // Get all workouts
    public Object getAllWorkouts() {
        String url = apiGatewayUrl + BASE_PATH + "/all";
        ResponseEntity<Object> response = restTemplate.exchange(
                url, HttpMethod.GET, null, Object.class
        );
        return response.getBody();
    }

    // Get a specific workout by userCode
    public Object getWorkout(String userCode) {
        String url = apiGatewayUrl + BASE_PATH + "?userCode=" + userCode;
        ResponseEntity<Object> response = restTemplate.exchange(
                url, HttpMethod.GET, null, Object.class
        );
        return response.getBody();
    }

    // Create a new workout
    public Object createWorkout(Object workoutRequest) {
        String url = apiGatewayUrl + BASE_PATH;
        HttpEntity<Object> requestEntity = new HttpEntity<>(workoutRequest);
        ResponseEntity<Object> response = restTemplate.exchange(
                url, HttpMethod.POST, requestEntity, Object.class
        );
        return response.getBody();
    }

    // Get workout by userCode
    public Object getWorkoutByUserCode(String userCode) {
        String url = apiGatewayUrl + BASE_PATH + "?userCode=" + userCode;
        ResponseEntity<Object> response = restTemplate.exchange(
                url, HttpMethod.GET, null, Object.class
        );
        return response.getBody();
    }

    // Update a workout
//    public void updateWorkout(Object workoutRequest) {
//        String userCode = ((WorkoutRequest) workoutRequest).getUserCode();
//        String url = apiGatewayUrl + BASE_PATH + "?userCode=" + userCode;
//        HttpEntity<Object> requestEntity = new HttpEntity<>(workoutRequest);
//        restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Void.class);
//    }

    // Delete a workout by userCode
//    public void deleteWorkout(String userCode) {
//        String url = apiGatewayUrl + BASE_PATH + "?userCode=" + userCode;
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(tokenService.getToken());
//        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
//
//        restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, Void.class);
//    }
}