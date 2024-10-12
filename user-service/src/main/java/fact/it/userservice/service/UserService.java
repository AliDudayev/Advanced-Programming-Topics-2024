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

    @PostConstruct
    public void loadData() {
        if(userRepository.count() <= 0){
            User record = User.builder()
                    .userCode("user1")
                    .name("John Doe")
                    .age(25)
                    .height(1.80)
                    .weight(80)
                    .email("test")
                    .phoneNr("test")
                    .build();
            userRepository.save(record);
        }
    }


    // get records
    public RecordResponse[] getAllRecords( ) {
        List<User> users = userRepository.findAll();

        // I want to get all the userCodes from the users and put them in a list
        List<String> userCodes = users.stream()
                .map(User::getUserCode)
                .toList();

        RecordResponse[] recordResponseArray = webClient.get()
                .uri("http://localhost:8082/api/record",
                        uriBuilder -> uriBuilder.queryParam("codes", userCodes).build())
                .retrieve()
                .bodyToMono(RecordResponse[].class)
                .block();


        /*

                */

//        return recordResponseArray
//                .stream()
//                .collect(Collectors.toList());

//        return mapToRecordResponse(records);

        return recordResponseArray;
    }

    private List<UserLineItemDto> MapToUserLineItemsDto(List<UserLineItem> userLineItems) {
        return userLineItems.stream()
                .map(userLineItem -> new UserLineItemDto(
                        userLineItem.getId(),
                        userLineItem.getUserCode(),
                        userLineItem.getFastestTime(),
                        userLineItem.getLongestDistance(),
                        userLineItem.getMaxWeightLifted(),
                        userLineItem.getLongestWorkoutDuration(),
                        userLineItem.getMostCaloriesBurned()
                ))
                .collect(Collectors.toList());
    }


    // get user by id
    public UserResponse getUserById(String id) {
        User user = userRepository.findById(id).orElseThrow();

        return mapToRecordResponse(user);
    }

    // Save the updated user
    public void updateUser(String userCode, UserRequest userRequest) {
        User user = userRepository.findByUserCode(userCode);

        user.setName(userRequest.getName());
        user.setAge(userRequest.getAge());
        user.setHeight(userRequest.getHeight());
        user.setWeight(userRequest.getWeight());
        user.setEmail(userRequest.getEmail());
        user.setPhoneNr(userRequest.getPhoneNr());
        user.setGender(userRequest.isGender());
        user.setFitnessGoals(userRequest.getFitnessGoals());


        userRepository.save(user);
    }

    // Delete record by id

    private UserResponse mapToRecordResponse(User user) {
        return UserResponse.builder()
                .name(user.getName())
                .userCode(user.getUserCode())
                .height(user.getHeight())
                .weight(user.getWeight())
                .email(user.getEmail())
                .fitnessGoals(user.getFitnessGoals())
                .build();
    }

}
