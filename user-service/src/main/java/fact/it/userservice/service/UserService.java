package fact.it.userservice.service;

import fact.it.userservice.dto.RecordResponse;
import fact.it.userservice.dto.UserLineItemDto;
import fact.it.userservice.dto.UserRequest;
import fact.it.userservice.dto.UserResponse;
import fact.it.userservice.model.UserLineItem;
import fact.it.userservice.model.User;
import fact.it.userservice.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final WebClient webClient;

    @Value("${RECORD_SERVICE_URL:http://localhost:8082}")
    private String recordServiceUrl;

    // create user --> Klaar
    public void createUser(UserRequest userRequest) {
        if(userRepository.findByUserCode(userRequest.getUserCode()) == null) {
            User user = User.builder()
                    .userCode(userRequest.getUserCode())
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
        }
        else {
            log.info("User with userCode: " + userRequest.getUserCode() + " already exists");
        }
    }

    // Get user by code --> Klaar
    public UserResponse getUserByCode(String userCode) {
        User user = userRepository.findByUserCode(userCode);
        return mapToUserResponse(user);
    }

    // Get all users --> Klaar
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    // Save the updated user --> Klaar
    public void updateUser(String userCode, UserRequest userRequest) {
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

    // Delete user by code --> Klaar
    public UserResponse deleteUser(String code) {
        User user = userRepository.findByUserCode(code);
        userRepository.delete(user);

        return mapToUserResponse(user);
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .userCode(user.getUserCode())
                .height(user.getHeight())
                .weight(user.getWeight())
                .email(user.getEmail())
                .male(user.isMale())
                .fitnessGoals(user.getFitnessGoals())
                .build();
    }

        // get records
    public RecordResponse getRecordOfUser(String userCode) {
        User user = userRepository.findByUserCode(userCode);

        RecordResponse recordResponse = webClient.get()
                .uri(recordServiceUrl + "/api/record",
                        uriBuilder -> uriBuilder.queryParam("userCode", userCode).build())
                .retrieve()
                .bodyToMono(RecordResponse.class)
                .block();

        return recordResponse;
    }








//    private List<UserLineItemDto> MapToUserLineItemsDto(List<UserLineItem> userLineItems) {
//        return userLineItems.stream()
//                .map(userLineItem -> new UserLineItemDto(
//                        userLineItem.getId(),
//                        userLineItem.getUserCode(),
//                        userLineItem.getFastestTime(),
//                        userLineItem.getLongestDistance(),
//                        userLineItem.getMaxWeightLifted(),
//                        userLineItem.getLongestWorkoutDuration(),
//                        userLineItem.getMostCaloriesBurned()
//                ))
//                .collect(Collectors.toList());
//    }

}
