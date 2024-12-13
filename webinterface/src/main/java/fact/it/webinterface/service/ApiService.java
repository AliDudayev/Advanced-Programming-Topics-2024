package fact.it.webinterface.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class ApiService {

    private final WebClient webClient;
    private final TokenService tokenService;

    @Value("${api.gateway.url}")
    private String apiGatewayUrl;

    public ApiService(WebClient webClient, TokenService tokenService) {
        this.webClient = webClient;
        this.tokenService = tokenService;
    }

    // Generic GET request
    public <T> T get(String path, Class<T> responseType) {
        String url = apiGatewayUrl + path;

        try {
            return webClient.get()
                    .uri(url)
                    .headers(headers -> headers.setBearerAuth(tokenService.getToken()))
                    .retrieve()
                    .bodyToMono(responseType)
                    .block(); // Synchronous call, consider using Mono<T> for async behavior
        } catch (WebClientResponseException e) {
            // Handle error responses (e.g., 4xx, 5xx)
            throw new RuntimeException("API call failed with status: " + e.getStatusCode(), e);
        }
    }

    // Generic POST request
    public <T> T post(String path, Object request, Class<T> responseType) {
        String url = apiGatewayUrl + path;

        try {
            return webClient.post()
                    .uri(url)
                    .headers(headers -> headers.setBearerAuth(tokenService.getToken()))
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(responseType)
                    .block(); // Synchronous call, consider returning Mono<T> for async behavior
        } catch (WebClientResponseException e) {
            // Handle error responses (e.g., 4xx, 5xx)
            throw new RuntimeException("API call failed with status: " + e.getStatusCode(), e);
        }
    }

    // Generic PUT request
    public <T> T put(String path, Object request, Class<T> responseType) {
        String url = apiGatewayUrl + path;

        try {
            return webClient.put()
                    .uri(url)
                    .headers(headers -> headers.setBearerAuth(tokenService.getToken()))
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(responseType)
                    .block(); // Synchronous call, consider returning Mono<T> for async behavior
        } catch (WebClientResponseException e) {
            // Handle error responses (e.g., 4xx, 5xx)
            throw new RuntimeException("API call failed with status: " + e.getStatusCode(), e);
        }
    }

    // Generic DELETE request
    public void delete(String path) {
        String url = apiGatewayUrl + path;

        try {
            webClient.delete()
                    .uri(url)
                    .headers(headers -> headers.setBearerAuth(tokenService.getToken()))
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block(); // Synchronous call, consider returning Mono<Void> for async behavior
        } catch (WebClientResponseException e) {
            // Handle error responses (e.g., 4xx, 5xx)
            throw new RuntimeException("API call failed with status: " + e.getStatusCode() , e);
        }
    }
}
