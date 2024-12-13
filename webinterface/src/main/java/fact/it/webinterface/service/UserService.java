package fact.it.webinterface.service;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final ApiService apiService;
    private static final String BASE_PATH = "/user";

    public UserService(ApiService apiService) {
        this.apiService = apiService;
    }

    // Get all users
    public Object getAllUsers() {
        return apiService.get(BASE_PATH + "/all", Object.class);  // GET /user/all
    }

    // Get a specific user by userCode
    public Object getUser(String userCode) {
        return apiService.get(BASE_PATH + "?userCode=" + userCode, Object.class);  // GET /user?userCode=xxx
    }

    // Create a new user
    public Object createUser(Object userRequest) {
        return apiService.post(BASE_PATH, userRequest, Object.class);
    }

    // Update a user
    public void updateUser(Object userRequest) {
        apiService.put(BASE_PATH, userRequest, Object.class);
    }

    // Delete a user by userCode
    public void deleteUser(String userCode) {
        apiService.delete(BASE_PATH + "?userCode=" + userCode);  // DELETE /user?userCode=xxx
    }
}
