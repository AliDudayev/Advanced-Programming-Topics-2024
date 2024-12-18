package fact.it.webinterface.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fact.it.webinterface.dto.UserRequest;
import fact.it.webinterface.dto.WorkoutRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

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
    public List<WorkoutRequest> getAllWorkouts() {
        String url = apiGatewayUrl + BASE_PATH + "/all";
        List<?> response = restTemplate.getForObject(url, List.class);

        System.out.println(url);
        System.out.println(response);

        ObjectMapper objectMapper = new ObjectMapper();
        List<WorkoutRequest> workouts = objectMapper.convertValue(response, new TypeReference<List<WorkoutRequest>>() {});
        return workouts;
    }

    // Get a specific workout by userCode
    public List<WorkoutRequest> getWorkoutByUserCode(String userCode) {
        String url = apiGatewayUrl + BASE_PATH + "?userCode=" + userCode;
        List<?> response = restTemplate.getForObject(url, List.class);

        System.out.println(url);
        System.out.println(response);

        // Use ObjectMapper to convert the response into a UserRequest object
        ObjectMapper objectMapper = new ObjectMapper();
        List<WorkoutRequest> workouts = objectMapper.convertValue(response, new TypeReference<List<WorkoutRequest>>() {});
        return workouts;
    }

    // Create a new workout
    public boolean createWorkout(WorkoutRequest workoutRequest) {
        String url = apiGatewayUrl + BASE_PATH;

        // Add token to the header
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenService.getToken());  // Add Bearer token

        // Create the HttpEntity with the user data and headers
        HttpEntity<WorkoutRequest> requestEntity = new HttpEntity<>(workoutRequest, headers);
        ResponseEntity<Void> response = restTemplate.exchange(
                url, HttpMethod.POST, requestEntity, Void.class
        );

        // Print the response status code for debugging
        System.out.println(url);
        System.out.println(response.getStatusCode());

        // Check for HttpStatus.OK (200)
        return response.getStatusCode() == HttpStatus.OK;
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