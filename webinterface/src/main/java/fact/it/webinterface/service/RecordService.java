package fact.it.webinterface.service;

import fact.it.webinterface.dto.RecordRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RecordService {

    private final RestTemplate restTemplate;
    private static final String BASE_PATH = "/record";

    @Value("${api.gateway.url}")
    private String apiGatewayUrl;

    public RecordService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Get all records
    public Object getAllRecords() {
        String url = apiGatewayUrl + BASE_PATH + "/all";
        ResponseEntity<Object> response = restTemplate.exchange(
                url, HttpMethod.GET, null, Object.class
        );
        return response.getBody();
    }
}