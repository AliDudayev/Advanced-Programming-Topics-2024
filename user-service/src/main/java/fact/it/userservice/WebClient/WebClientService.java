package fact.it.userservice.WebClient;

import fact.it.userservice.dto.RecordResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class WebClientService {

    private final WebClient webClient;

    @Value("${recordService.baseurl}")
    private String recordServiceUrl;

    @Value("${workoutService.baseurl}")
    private String workoutServiceUrl;


    public WebClientService(WebClient webClient) {
        this.webClient = webClient;
    }

    // Create a record for a specific user
    public void createRecord(String userCode, RecordResponse recordResponse) {
        webClient.post()
                .uri(recordServiceUrl + "/api/record", uriBuilder -> uriBuilder.queryParam("userCode", userCode).build())
                .bodyValue(recordResponse)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    // Update a record for a specific user
    public void updateRecord(String userCode, RecordResponse recordResponse) {
        webClient.put()
                .uri(recordServiceUrl + "/api/record", uriBuilder -> uriBuilder.queryParam("userCode", userCode).build())
                .bodyValue(recordResponse)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    // Delete a record for a specific user
    public void deleteRecord(String userCode) {
        webClient.delete()
                .uri(recordServiceUrl + "/api/record", uriBuilder -> uriBuilder.queryParam("userCode", userCode).build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    // Fetch a specific user's record (optional, if needed)
    public RecordResponse getRecord(String userCode) {
        return webClient.get()
                .uri(recordServiceUrl + "/api/record", uriBuilder -> uriBuilder.queryParam("userCode", userCode).build())
                .retrieve()
                .bodyToMono(RecordResponse.class)
                .block();
    }
}
