package fact.it.webinterface.service;

import fact.it.webinterface.dto.HealthRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HealthService {

    private final RestTemplate restTemplate;
    private static final String BASE_PATH = "/health";

    @Value("${api.gateway.url}")
    private String apiGatewayUrl;

    public HealthService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Get all healths
    public Object getAllHealth() {
        String url = apiGatewayUrl + BASE_PATH + "/all";
        ResponseEntity<Object> response = restTemplate.exchange(
                url, HttpMethod.GET, null, Object.class
        );
        return response.getBody();
    }

    // Get a specific health by workoutCode
    public Object getHealth(String workoutCode) {
        String url = apiGatewayUrl + BASE_PATH + "?workoutCode=" + workoutCode;
        ResponseEntity<Object> response = restTemplate.exchange(
                url, HttpMethod.GET, null, Object.class
        );
        return response.getBody();
    }

    // Create a new health
    public Object createHealth(Object healthRequest) {
        String url = apiGatewayUrl + BASE_PATH;
        HttpEntity<Object> requestEntity = new HttpEntity<>(healthRequest);
        ResponseEntity<Object> response = restTemplate.exchange(
                url, HttpMethod.POST, requestEntity, Object.class
        );
        return response.getBody();
    }

    // Update a health
    public void updateHealth(Object healthRequest) {
        String workoutCode = ((HealthRequest) healthRequest).getWorkoutCode();
        String url = apiGatewayUrl + BASE_PATH + "?workoutCode=" + workoutCode;
        HttpEntity<Object> requestEntity = new HttpEntity<>(healthRequest);
        restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Void.class);
    }

}
