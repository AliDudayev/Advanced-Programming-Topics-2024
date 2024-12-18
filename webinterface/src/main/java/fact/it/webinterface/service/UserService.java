package fact.it.webinterface.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fact.it.webinterface.dto.UserRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class UserService {

    private final RestTemplate restTemplate;
    private final TokenService tokenService; // Needed only for delete
    private static final String BASE_PATH = "/user";

    @Value("${api.gateway.url}")
    private String apiGatewayUrl;

    public UserService(RestTemplate restTemplate, TokenService tokenService) {
        this.restTemplate = restTemplate;
        this.tokenService = tokenService;
    }

    // Get all users
    public List<UserRequest> getAllUsers() {
        String url = apiGatewayUrl + BASE_PATH + "/all";
        List<?> response = restTemplate.getForObject(url, List.class);

        // Print the response for debugging
        System.out.println(url);
        System.out.println(response);

        // Use ObjectMapper to convert the response into a list of UserRequest objects
        ObjectMapper objectMapper = new ObjectMapper();
        List<UserRequest> users = objectMapper.convertValue(response, new TypeReference<List<UserRequest>>() {});
        return users;
    }

    // Get a specific user by userCode
    public UserRequest getUser(String userCode) {
        String url = apiGatewayUrl + BASE_PATH + "?userCode=" + userCode;
        ResponseEntity<?> response = restTemplate.exchange(
                url, HttpMethod.GET, null, Object.class
        );

        // Print the response for debugging
        System.out.println(url);
        System.out.println(response.getBody());

        // Use ObjectMapper to convert the response into a UserRequest object
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(response.getBody(), UserRequest.class);
    }

    // Create a new user
    public boolean createUser(UserRequest userRequest) {
        String url = apiGatewayUrl + BASE_PATH;
        HttpEntity<UserRequest> requestEntity = new HttpEntity<>(userRequest);
        ResponseEntity<Void> response = restTemplate.exchange(
                url, HttpMethod.POST, requestEntity, Void.class
        );

        // Print the response status code for debugging
        System.out.println(url);
        System.out.println(response.getStatusCode());

        // Check for HttpStatus.OK (200)
        return response.getStatusCode() == HttpStatus.OK;
    }

    // Update a user
    public boolean updateUser(UserRequest userRequest) {
        String userCode = userRequest.getUserCode();
        String url = apiGatewayUrl + BASE_PATH + "?userCode=" + userCode;
        HttpEntity<UserRequest> requestEntity = new HttpEntity<>(userRequest);
        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Void.class);

        // Print the response status code for debugging
        System.out.println(url);
        System.out.println(response.getStatusCode());

        // Check for HttpStatus.OK (200)
        return response.getStatusCode() == HttpStatus.OK;
    }

    // Delete a user by userCode
    public boolean deleteUser(String userCode) {
        String url = apiGatewayUrl + BASE_PATH + "?userCode=" + userCode;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenService.getToken());
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, Void.class);

        // Print the response status code for debugging
        System.out.println(url);
        System.out.println(response.getStatusCode());

        // Check for HttpStatus.OK (200)
        return response.getStatusCode() == HttpStatus.OK;
    }
}
