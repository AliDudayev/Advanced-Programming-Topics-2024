package fact.it.webinterface.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fact.it.webinterface.dto.HealthRequest;
import fact.it.webinterface.dto.UserRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class HealthService {

    private final RestTemplate restTemplate;

    private final TokenService tokenService;
    private static final String BASE_PATH = "/health";

    @Value("${api.gateway.url}")
    private String apiGatewayUrl;

    public HealthService(RestTemplate restTemplate, TokenService tokenService) {
        this.restTemplate = restTemplate;
        this.tokenService = tokenService;
    }

    // Get all healths
    public List<HealthRequest> getAllHealth() {
        String url = apiGatewayUrl + BASE_PATH + "/all";
        List<?> response = restTemplate.getForObject(url, List.class);

        System.out.println(response);
        System.out.println(url);

        ObjectMapper objectMapper = new ObjectMapper();
        List<HealthRequest> healths = objectMapper.convertValue(response, new TypeReference<List<HealthRequest>>() {});
        return healths;
    }

    // Get a specific health by workoutCode
    public HealthRequest getHealth(String workoutCode) {
        String url = apiGatewayUrl + BASE_PATH + "?workoutCode=" + workoutCode;
        ResponseEntity<?> response = restTemplate.exchange(
                url, HttpMethod.GET, null, Object.class
        );

        System.out.println(response);
        System.out.println(url);

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(response.getBody(), HealthRequest.class);
    }

    // Create a new health
    public boolean createHealth(HealthRequest healthRequest) {
        String url = apiGatewayUrl + BASE_PATH;
        System.out.println(url);

        // Add token to the header
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenService.getToken());  // Add Bearer token

        HttpEntity<HealthRequest> requestEntity = new HttpEntity<>(healthRequest, headers);
        ResponseEntity<Void> response = restTemplate.exchange(
                url, HttpMethod.POST, requestEntity, Void.class
        );
        System.out.println(response.getBody());
        System.out.println(response.getStatusCode());
        return response.getStatusCode() == HttpStatus.OK;
    }

    // Update a health
    public boolean updateHealth(HealthRequest healthRequest) {
        String workoutCode = healthRequest.getWorkoutCode();
        String url = apiGatewayUrl + BASE_PATH + "?workoutCode=" + workoutCode;
        System.out.println(url);

        // Add token to the header
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenService.getToken());  // Add Bearer token

        HttpEntity<HealthRequest> requestEntity = new HttpEntity<>(healthRequest, headers);
        ResponseEntity<Void> response = restTemplate.exchange(
                url, HttpMethod.PUT, requestEntity, Void.class
        );
        System.out.println(response.getBody());
        System.out.println(response.getStatusCode());

        return response.getStatusCode() == HttpStatus.OK;
    }

}
