package fact.it.webinterface.service;

import fact.it.webinterface.dto.UserRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
    public Object getAllUsers() {
        String url = apiGatewayUrl + BASE_PATH + "/all";
        ResponseEntity<Object> response = restTemplate.exchange(
                url, HttpMethod.GET, null, Object.class
        );
        return response.getBody();
    }

    // Get a specific user by userCode
    public Object getUser(String userCode) {
        String url = apiGatewayUrl + BASE_PATH + "?userCode=" + userCode;
        ResponseEntity<Object> response = restTemplate.exchange(
                url, HttpMethod.GET, null, Object.class
        );
        return response.getBody();
    }

    // Create a new user
    public Object createUser(Object userRequest) {
        String url = apiGatewayUrl + BASE_PATH;
        HttpEntity<Object> requestEntity = new HttpEntity<>(userRequest);
        ResponseEntity<Object> response = restTemplate.exchange(
                url, HttpMethod.POST, requestEntity, Object.class
        );
        return response.getBody();
    }

    // Update a user
    public void updateUser(Object userRequest) {
        String userCode = ((UserRequest) userRequest).getUserCode();
        String url = apiGatewayUrl + BASE_PATH + "?userCode=" + userCode;
        HttpEntity<Object> requestEntity = new HttpEntity<>(userRequest);
        restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Void.class);
    }

    // Delete a user by userCode
    public void deleteUser(String userCode) {
        String url = apiGatewayUrl + BASE_PATH + "?userCode=" + userCode;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenService.getToken());
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, Void.class);
    }
}
