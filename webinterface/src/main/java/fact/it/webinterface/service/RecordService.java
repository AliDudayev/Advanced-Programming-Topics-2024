package fact.it.webinterface.service;

import fact.it.webinterface.dto.RecordRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RecordService {

    private final RestTemplate restTemplate;
    private final TokenService tokenService; // Needed only for delete
    private static final String BASE_PATH = "/record";

    @Value("${api.gateway.url}")
    private String apiGatewayUrl;

    public RecordService(RestTemplate restTemplate, TokenService tokenService) {
        this.restTemplate = restTemplate;
        this.tokenService = tokenService;
    }

    // Get all records
    public Object getAllRecords() {
        String url = apiGatewayUrl + BASE_PATH + "/all";
        ResponseEntity<Object> response = restTemplate.exchange(
                url, HttpMethod.GET, null, Object.class
        );
        return response.getBody();
    }

    // Get a specific record by recordCode
    public Object getRecord(String userCode) {
        String url = apiGatewayUrl + BASE_PATH + "?userCode=" + userCode;
        ResponseEntity<Object> response = restTemplate.exchange(
                url, HttpMethod.GET, null, Object.class
        );
        return response.getBody();
    }

    // Create a new record
    public Object createRecord(Object recordRequest) {
        String url = apiGatewayUrl + BASE_PATH;
        HttpEntity<Object> requestEntity = new HttpEntity<>(recordRequest);
        ResponseEntity<Object> response = restTemplate.exchange(
                url, HttpMethod.POST, requestEntity, Object.class
        );
        return response.getBody();
    }

    // Update a record
    public void updateRecord(Object recordRequest) {
        String userCode = ((RecordRequest) recordRequest).getUserCode();
        String url = apiGatewayUrl + BASE_PATH + "?userCode=" + userCode;
        HttpEntity<Object> requestEntity = new HttpEntity<>(recordRequest);
        restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Void.class);
    }

    // Delete a record by recordCode
    public void deleteRecord(String userCode) {
        String url = apiGatewayUrl + BASE_PATH + "?userCode=" + userCode;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenService.getToken());
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, Void.class);
    }
}