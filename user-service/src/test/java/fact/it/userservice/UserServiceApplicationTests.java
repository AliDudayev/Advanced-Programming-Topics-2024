package fact.it.userservice;

import fact.it.userservice.dto.RecordResponse;
import fact.it.userservice.dto.UserRequest;
import fact.it.userservice.dto.UserResponse;
import fact.it.userservice.dto.WorkoutResponse;
import fact.it.userservice.model.User;
import fact.it.userservice.repository.UserRepository;
import fact.it.userservice.service.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceUnitTests {
    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    private RequestBodySpec requestBodySpec;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(userService, "recordServiceUrl", "http://localhost:8082");
        ReflectionTestUtils.setField(userService, "workoutServiceUrl", "http://localhost:8083");
    }

    @Order(1)
    @Test
    void createUser_WithValidRequest_UserIsSaved() {
        // Arrange
        UserRequest userRequest = new UserRequest("Test User", "UserCode-123", 25, 1.80, 75.0, "123456789", true, "testuser@example.com", "Build muscle");

        User user = User.builder()
                .userCode(userRequest.getUserCode())
                .name(userRequest.getName())
                .age(userRequest.getAge())
                .height(userRequest.getHeight())
                .weight(userRequest.getWeight())
                .phoneNr(userRequest.getPhoneNr())
                .email(userRequest.getEmail())
                .male(userRequest.isMale())
                .fitnessGoals(userRequest.getFitnessGoals())
                .build();

        when(userRepository.save(any(User.class))).thenReturn(user);

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any(RecordResponse.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Void.class)).thenReturn(Mono.empty());

        // Capture arguments
        ArgumentCaptor<RecordResponse> recordCaptor = ArgumentCaptor.forClass(RecordResponse.class);

        // Act
        userService.createUser(userRequest);

        // Assert
        verify(userRepository, times(1)).save(any(User.class));
        verify(requestBodySpec, times(1)).bodyValue(recordCaptor.capture());
        RecordResponse capturedRecord = recordCaptor.getValue();

        assertNotNull(capturedRecord);
        assertEquals(10000.0, capturedRecord.getFastestTime());
    }

    @Order(2)
    @Test
    void getUserByCode_UserExists_ReturnsUserResponse() {
        // Arrange
        User user = User.builder()
                .id("1")
                .userCode("UserCode-123")
                .name("Test User")
                .age(25)
                .height(1.80)
                .weight(75.0)
                .phoneNr("123456789")
                .male(true)
                .email("testuser@example.com")
                .fitnessGoals("Build muscle")
                .build();

        when(userRepository.findByUserCode(anyString())).thenReturn(user);

        // Act
        UserResponse userResponse = userService.getUserByCode(user.getUserCode());

        // Assert
        assertNotNull(userResponse);
        assertEquals("Test User", userResponse.getName());
        assertEquals("UserCode-123", userResponse.getUserCode());
        verify(userRepository, times(2)).findByUserCode(user.getUserCode());
    }

    @Order(3)
    @Test
    void getUserByCode_UserDoesNotExist_ReturnsNull() {
        // Arrange
        User user = User.builder()
                .id("1")
                .userCode("UserCode-123")
                .name("Test User")
                .age(25)
                .height(1.80)
                .weight(75.0)
                .phoneNr("123456789")
                .male(true)
                .email("testuser@example.com")
                .fitnessGoals("Build muscle")
                .build();

        when(userRepository.findByUserCode(anyString())).thenReturn(null);

        // Act
        UserResponse userResponse = userService.getUserByCode(user.getUserCode());

        // Assert
        assertNull(userResponse);
        verify(userRepository, times(1)).findByUserCode(user.getUserCode());
    }

    @Order(4)
    @Test
    void getAllUsers_ReturnsListOfUsers() {
        // Arrange
        User user1 = User.builder()
                .userCode("UserCode-123")
                .name("User 1")
                .build();
        User user2 = User.builder()
                .userCode("UserCode-456")
                .name("User 2")
                .build();

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        // Act
        List<UserResponse> userResponses = userService.getAllUsers();

        // Assert
        assertNotNull(userResponses);
        assertEquals(2, userResponses.size());
        verify(userRepository, times(1)).findAll();
        assertEquals("UserCode-123", userResponses.get(0).getUserCode());
        assertEquals("UserCode-456", userResponses.get(1).getUserCode());
    }

    @Order(5)
    @Test
    void updateUser_UserExists_UpdatesUser() {
        // Arrange
        UserRequest newUserValues = UserRequest.builder()
                .name("New Name")
                .age(26)
                .height(1.81)
                .weight(76)
                .phoneNr("987654321")
                .male(true)
                .email("newUser@example.com")
                .fitnessGoals("Maintain fitness")
                .build();

        User existingUser = User.builder()
                .userCode("UserCode-123")
                .name("Old Name")
                .age(25)
                .height(1.80)
                .weight(75.0)
                .phoneNr("123456789")
                .male(true)
                .email("olduser@example.com")
                .fitnessGoals("Build muscle")
                .build();

        when(userRepository.findByUserCode(anyString())).thenReturn(existingUser);
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        // Act
        userService.updateUser(existingUser.getUserCode(), newUserValues);

        // Assert
        verify(userRepository, times(1)).save(any(User.class));  // Verify that save was called once
        assertEquals("New Name", existingUser.getName());
        assertEquals(26, existingUser.getAge());
        assertEquals(1.81, existingUser.getHeight());
        assertEquals(76.0, existingUser.getWeight());
        assertEquals("987654321", existingUser.getPhoneNr());
        assertEquals("newUser@example.com", existingUser.getEmail());
        assertEquals("Maintain fitness", existingUser.getFitnessGoals());
    }

    @Order(6)
    @Test
    void updateUser_UserDoesNotExists_ReturnsNull() {
        // Arrange
        UserRequest existingUser = new UserRequest("Updated Name", "UserCode-123", 26, 1.81, 76.0, "987654321", true, "updateduser@example.com", "Maintain fitness");

        when(userRepository.findByUserCode(anyString())).thenReturn(null);

        // Act
        userService.updateUser(existingUser.getUserCode(), existingUser);

        // Assert
        verify(userRepository, times(1)).findByUserCode(existingUser.getUserCode());
        verify(userRepository, times(0)).save(any(User.class));  // Verify that save was called once
    }

    @Order(7)
    @Test
    void getRecordOfUser_WithValidRequest_RecordIsReturned() {
        // Arrange
        UserRequest userRequest = new UserRequest("Test User", "UserCode-123", 25, 1.80, 75.0, "123456789", true, "testuser@example.com", "Build muscle");

        RecordResponse recordResponse = new RecordResponse("1", "UserCode-123", 10000.0, 100.0, 50.0, 60.0, 0.0);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(RecordResponse.class)).thenReturn(Mono.just(recordResponse));

        // Act
        RecordResponse result = userService.getRecordOfUser(userRequest.getUserCode());

        // Then
        assertNotNull(result);
        assertEquals(recordResponse.getUserCode(), result.getUserCode());
        assertEquals(10000.0, result.getFastestTime());
        assertEquals(100.0, result.getLongestDistance());
        assertEquals(50.0, result.getMaxWeightLifted());
        assertEquals(60.0, result.getLongestWorkoutDuration());
    }

    @Order(8)
    @Test
    void getAllRecords_WithValidRequest_RecordsAreReturned() {
        // Arrange
        RecordResponse recordResponse1 = new RecordResponse("1", "UserCode-123", 10000.0, 100.0, 60.0, 20.0, 800.0);
        RecordResponse recordResponse2 = new RecordResponse("2", "UserCode-456", 20000.0, 50.0, 80.0, 70.0, 1000.0);
        List<RecordResponse> recordList = List.of(recordResponse1, recordResponse2);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(RecordResponse.class)).thenReturn(Flux.fromIterable(recordList));

        // Act
        List<RecordResponse> recordResponseList = userService.getAllRecords();

        // Then
        assertNotNull(recordResponseList);
        assertEquals(2, recordResponseList.size());
        assertEquals("UserCode-123", recordResponseList.get(0).getUserCode());
        assertEquals("UserCode-456", recordResponseList.get(1).getUserCode());
        assertEquals(10000.0, recordResponseList.get(0).getFastestTime());
        assertEquals(20000.0, recordResponseList.get(1).getFastestTime());
        assertEquals(100.0, recordResponseList.get(0).getLongestDistance());
        assertEquals(50.0, recordResponseList.get(1).getLongestDistance());
        assertEquals(60.0, recordResponseList.get(0).getMaxWeightLifted());
        assertEquals(80.0, recordResponseList.get(1).getMaxWeightLifted());
        assertEquals(20.0, recordResponseList.get(0).getLongestWorkoutDuration());
        assertEquals(70.0, recordResponseList.get(1).getLongestWorkoutDuration());
        assertEquals(800.0, recordResponseList.get(0).getMostCaloriesBurned());
        assertEquals(1000.0, recordResponseList.get(1).getMostCaloriesBurned());
    }

    @Order(9)
    @Test
    void createRecord_WithValidRequest_UserIsSaved() {
        // Arrange
        UserRequest userRequest = new UserRequest("Test User", "UserCode-123", 25, 1.80, 75.0, "123456789", true, "testuser@example.com", "Build muscle");

        RecordResponse recordResponse = new RecordResponse("1", "UserCode-123", 10000.0, 0.0, 0.0, 0.0, 0.0);

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any(RecordResponse.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Void.class)).thenReturn(Mono.empty());

        // Capture arguments
        ArgumentCaptor<RecordResponse> recordCaptor = ArgumentCaptor.forClass(RecordResponse.class);

        // Act
        userService.createRecord(userRequest.getUserCode(), recordResponse);

        verify(requestBodySpec, times(1)).bodyValue(recordCaptor.capture()); // Capture the argument
        RecordResponse capturedRecord = recordCaptor.getValue();

        // Validate the captured value
        assertNotNull(capturedRecord);
        assertEquals("UserCode-123", capturedRecord.getUserCode());
        assertEquals(10000.0, capturedRecord.getFastestTime());
    }

    @Order(10)
    @Test
    void deleteUser_UserExists_DeletesUser() {
        // Arrange
        String userCode = "UserCode-123";
        User user = User.builder()
                .userCode(userCode)
                .name("Test User")
                .build();

        when(userRepository.findByUserCode(userCode)).thenReturn(user);

        when(webClient.delete()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Void.class)).thenReturn(Mono.empty());

        // Act
        UserResponse deletedUser = userService.deleteUser(userCode);

        // Assert
        assertNotNull(deletedUser);
        verify(userRepository, times(1)).delete(any(User.class));
    }

    @Order(11)
    @Test
    void deleteUser_UserDoesNotExist_DeletesUser() {
        // Arrange
        String userCode = "UserCode-123";

        when(userRepository.findByUserCode(userCode)).thenReturn(null);

        // Act
        UserResponse deletedUser = userService.deleteUser(userCode);

        // Assert
        assertNull(deletedUser);
        verify(userRepository, times(0)).delete(any(User.class));
    }

    @Order(12)
    @Test
    void updateRecord_UserExists_DeletesUser() {
        // Arrange
        String userCode = "UserCode-123";

        RecordResponse recordResponse = new RecordResponse("1", "UserCode-123", 10000.0, 0.0, 0.0, 0.0, 0.0);

        when(webClient.put()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any(RecordResponse.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Void.class)).thenReturn(Mono.empty());

        // Capture arguments
        ArgumentCaptor<RecordResponse> recordCaptor = ArgumentCaptor.forClass(RecordResponse.class);

        // Act
        userService.updateRecord(userCode, recordResponse);

        verify(requestBodySpec, times(1)).bodyValue(recordCaptor.capture()); // Capture the argument
        RecordResponse capturedRecord = recordCaptor.getValue();

        assertNotNull(capturedRecord);
        assertEquals("UserCode-123", capturedRecord.getUserCode());
        assertEquals(10000.0, capturedRecord.getFastestTime());
    }
    @Order(13)
    @Test
    void getAllWorkoutsFromUser_WithValidRequest_WorkoutFromUserAreReturned() {
        // Arrange
        WorkoutResponse workoutResponse1 = new WorkoutResponse("1", "WorkoutCode-123", "Workout 1", "Gym");
        WorkoutResponse workoutResponse2 = new WorkoutResponse("2", "WorkoutCode-456", "Workout 2", "Track");

        List<WorkoutResponse> workoutList = List.of(workoutResponse1, workoutResponse2);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(WorkoutResponse.class)).thenReturn(Flux.fromIterable(workoutList));

        // Act
        List<WorkoutResponse> workoutResponseList = userService.getAllWorkoutsFromUser("UserCode-123");

        // Then
        assertNotNull(workoutResponseList);
        assertEquals(2, workoutResponseList.size());
        assertEquals("WorkoutCode-123", workoutResponseList.get(0).getWorkoutCode());
        assertEquals("WorkoutCode-456", workoutResponseList.get(1).getWorkoutCode());
    }

}
