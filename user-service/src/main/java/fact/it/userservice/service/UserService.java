package fact.it.userservice.service;

import fact.it.userservice.dto.RecordResponse;
import fact.it.userservice.dto.UserRequest;
import fact.it.userservice.dto.UserResponse;
import fact.it.userservice.dto.WorkoutResponse;
import fact.it.userservice.model.User;
import fact.it.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final WebClient webClient;

    @Value("${recordService.baseurl}")
    private String recordServiceUrl;

    @Value("${workoutService.baseurl}")
    private String workoutServiceUrl;

    public void createUser(UserRequest userRequest) {

        String userCode = userRequest.getUserCode() + "-" + UUID.randomUUID().toString().substring(0, 5);

        User user = User.builder()
                .userCode(userCode)
                .name(userRequest.getName())
                .age(userRequest.getAge())
                .height(userRequest.getHeight())
                .weight(userRequest.getWeight())
                .email(userRequest.getEmail())
                .phoneNr(userRequest.getPhoneNr())
                .male(userRequest.isMale())
                .fitnessGoals(userRequest.getFitnessGoals())
                .build();
        userRepository.save(user);

        RecordResponse recordResponse = RecordResponse.builder()
                .userCode(user.getUserCode())
                .fastestTime(10000.0)
                .longestDistance(0.0)
                .maxWeightLifted(0.0)
                .longestWorkoutDuration(0.0)
                .mostCaloriesBurned(0.0)
                .build();

        createRecord(user.getUserCode(), recordResponse);
    }

    public UserResponse getUserByCode(String userCode) {
        if(userRepository.findByUserCode(userCode) != null) {
            User user = userRepository.findByUserCode(userCode);
            return mapToUserResponse(user);
        }
        return null;
    }

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    public void updateUser(String userCode, UserRequest userRequest) {
        if(userRepository.findByUserCode(userCode) != null)
        {
            User user = userRepository.findByUserCode(userCode);

            user.setName(userRequest.getName());
            user.setAge(userRequest.getAge());
            user.setHeight(userRequest.getHeight());
            user.setWeight(userRequest.getWeight());
            user.setEmail(userRequest.getEmail());
            user.setPhoneNr(userRequest.getPhoneNr());
            user.setMale(userRequest.isMale());
            user.setFitnessGoals(userRequest.getFitnessGoals());

            userRepository.save(user);
        }
    }

    public UserResponse deleteUser(String userCode) {
        if (userRepository.findByUserCode(userCode) != null) {
            User user = userRepository.findByUserCode(userCode);
            userRepository.delete(user);
            deleteRecord(userCode);

            return mapToUserResponse(user);
        }
        return null;
    }

    public RecordResponse getRecordOfUser(String userCode) {

        RecordResponse recordResponse = webClient.get()
                .uri(recordServiceUrl + "/api/record?userCode=" + userCode)
                .retrieve()
                .bodyToMono(RecordResponse.class)
                .block();

        return recordResponse;
    }

    public List<RecordResponse> getAllRecords() {
        return webClient.get()
                .uri(recordServiceUrl + "/api/record/all")
                .retrieve()
                .bodyToFlux(RecordResponse.class)
                .collectList()
                .block();
    }

    public void updateRecord(String userCode, RecordResponse recordResponse) {

        webClient.put()
                .uri(recordServiceUrl + "/api/record?userCode=" + userCode)
                .bodyValue(recordResponse)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public void createRecord(String userCode, RecordResponse recordResponse) {
        webClient.post()
                .uri(recordServiceUrl + "/api/record?userCode=" + userCode)
                .bodyValue(recordResponse)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public void deleteRecord(String userCode) {

        webClient.delete()
                .uri(recordServiceUrl + "/api/record?userCode=" + userCode)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public List<WorkoutResponse> getAllWorkoutsFromUser(String userCode) {
        return webClient.get()
                .uri(workoutServiceUrl + "/api/workout/user?userCode=" + userCode)
                .retrieve()
                .bodyToFlux(WorkoutResponse.class)
                .collectList()
                .block();
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .age(user.getAge())
                .userCode(user.getUserCode())
                .height(user.getHeight())
                .weight(user.getWeight())
                .email(user.getEmail())
                .male(user.isMale())
                .fitnessGoals(user.getFitnessGoals())
                .build();
    }
}
