package fact.it.userservice;

import fact.it.userservice.dto.RecordResponse;
import fact.it.userservice.dto.UserRequest;
import fact.it.userservice.dto.UserResponse;
import fact.it.userservice.model.User;
import fact.it.userservice.repository.UserRepository;
import fact.it.userservice.service.UserService;
//import fact.it.userservice.utils.UUIDGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodyUriSpec;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import reactor.core.publisher.Mono;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.function.Function;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTests {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WebClient webClient; // Mock WebClient
//
//    @Mock
//    private RequestBodyUriSpec requestBodyUriSpec;
//
//    @Mock
//    private RequestBodySpec requestBodySpec;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private ResponseSpec responseSpec;

//    private UUIDGenerator uuidGenerator;
//    @Mock
//    private UUIDGenerator uuidGenerator; // Mock UUIDGenerator

    @BeforeEach
    void setUp() {

        // Set up the URLs for external services (if needed)
        ReflectionTestUtils.setField(userService, "recordServiceUrl", "http://localhost:8082");
        ReflectionTestUtils.setField(userService, "workoutServiceUrl", "http://localhost:8083");
    }

    @Test
    void createUser_WithValidRequest_UserIsSaved() {
        // Arrange
        UserRequest userRequest = UserRequest.builder()
                .name("Test User")
                .age(25)
                .height(1.80)
                .weight(75.0)
                .phoneNr("123456789")
                .male(true)
                .email("testuser@example.com")
                .fitnessGoals("Build muscle")
                .build();



        // Act
        userService.createUser(userRequest);

        // Assert
        verify(userRepository, times(1)).save(any(User.class));
    }

    /*
    @Test
    void createUser_WithValidRequest_UserIsSaved() {
        // Arrange
        UserRequest userRequest = UserRequest.builder()
                .name("Test User")
//                .userCode("User123")
                .age(25)
                .height(1.80)
                .weight(75.0)
                .phoneNr("123456789")
                .male(true)
                .email("testuser@example.com")
                .fitnessGoals("Build muscle")
                .build();

        // Mock the UUIDGenerator to return a fixed UUID
        when(uuidGenerator.generate()).thenReturn("UserCode-123");

        // Dit doen we zodat de user de juiste usercode heeft
        User mockUser = User.builder()
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

        when(userRepository.save(any(User.class))).thenReturn(mockUser);
        when(userRepository.findByUserCode("UserCode-123")).thenReturn(null);


        // Mock the WebClient for the external call to the RecordService
//        RecordResponse recordResponse = RecordResponse.builder()
//                .userCode("TestUser123")
//                .fastestTime(10000.0)
//                .longestDistance(0.0)
//                .maxWeightLifted(0.0)
//                .longestWorkoutDuration(0.0)
//                .mostCaloriesBurned(0.0)
//                .build();
//
//        when(webClient.get()).thenReturn(requestHeadersUriSpec);
//        when(requestHeadersUriSpec.uri(anyString(),  any(Function.class))).thenReturn(requestHeadersSpec);
//        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
//        when(responseSpec.bodyToMono(RecordResponse[].class)).thenReturn(Mono.just(new RecordResponse[]{recordResponse}));

        // Act
        userService.createUser(userRequest);

        // Assert
//        UserResponse userResponse = userService.getUserByCode("TestUser123");
//        assertEquals("TestUser123", userResponse.getUserCode());
//        assertEquals("Test User", userResponse.getName());

        // Verify that the repository save was called
        verify(userRepository, times(1)).save(any(User.class));

        // Verify that WebClient was called for external interaction
//        verify(webClient, times(1)).get(); // Ensure get() was called
//        verify(requestBodyUriSpec, times(1)).uri(anyString()); // Verify uri() was called
//        verify(requestBodySpec, times(1)).retrieve(); // Verify retrieve() was called
        assertEquals("UserCode-123", mockUser.getUserCode());  // Assert that the generated user code is correct
    }

    * */


    @Test
    void createUser_WithInvalidRequest_ThrowsException() {
        // Arrange
        UserRequest userRequest = UserRequest.builder()
                .name("Test User")
                .userCode("TestUser123")
                .age(-5)  // Invalid age
                .height(1.80)
                .weight(75.0)
                .phoneNr("123456789")
                .male(true)
                .email("testuser@example.com")
                .fitnessGoals("Build muscle")
                .build();

        // Act & Assert
        try {
            userService.createUser(userRequest);
        } catch (Exception e) {
            assertEquals("Invalid user details", e.getMessage());
        }

        // Verify that the repository save was never called
        verify(userRepository, never()).save(any(User.class));

        // Verify that WebClient was not called for this invalid case
        verify(webClient, never()).get();  // Ensure get() was not called
    }

    @Test
    void getUser_WithValidCode_ReturnsUser() {
        // Arrange
        String userCode = "TestUser123";
        User mockUser = User.builder()
                .userCode(userCode)
                .name("Test User")
                .age(25)
                .height(1.80)
                .weight(75.0)
                .phoneNr("123456789")
                .male(true)
                .email("testuser@example.com")
                .fitnessGoals("Build muscle")
                .build();

        when(userRepository.findByUserCode(userCode)).thenReturn(null);

        // Act
        UserResponse userResponse = userService.getUserByCode(userCode);

        // Assert
        assertNotNull(userResponse);
        assertEquals(userCode, userResponse.getUserCode());
        assertEquals("Test User", userResponse.getName());

        // Verify that the repository findByUserCode was called
        verify(userRepository, times(1)).findByUserCode(userCode);
    }

    @Test
    void getUser_WithInvalidCode_ThrowsException() {
        // Arrange
        String userCode = "InvalidCode";
        when(userRepository.findByUserCode(userCode)).thenReturn(null);

        // Act & Assert
        try {
            userService.getUserByCode(userCode);
        } catch (Exception e) {
            assertEquals("User not found", e.getMessage());
        }

        // Verify that the repository findByUserCode was called
        verify(userRepository, times(1)).findByUserCode(userCode);
    }
}
