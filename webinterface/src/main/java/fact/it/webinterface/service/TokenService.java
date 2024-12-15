package fact.it.webinterface.service;

import org.springframework.stereotype.Service;

@Service
public class TokenService {
    private String token;

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public boolean hasToken() {
        return token != null && !token.isEmpty();
    }
}
