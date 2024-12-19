package fact.it.userservice;

import fact.it.userservice.WebClient.WebClientService;
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
import org.springframework.web.reactive.function.client.WebClient.RequestBodyUriSpec;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import reactor.core.publisher.Mono;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.function.Function;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTests {

//    @InjectMocks
//    private UserService userService;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private WebClient webClient; // Mock WebClient
//

//
//    @Mock
//    private WebClient.RequestBodySpec requestBodySpec;        // Mock RequestBodySpec
//
//    @Mock
//    private WebClient.ResponseSpec responseSpec;              // Mock ResponseSpec
//
//    @BeforeEach
//    void setUp() {
//        // Set up the URLs for external services (if needed)
//        ReflectionTestUtils.setField(userService, "recordServiceUrl", "http://localhost:8082");
////        ReflectionTestUtils.setField(userService, "workoutServiceUrl", "http://localhost:8083");
//    }

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;  // Mock RequestBodyUriSpec

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WebClientService webClientService;  // Mock WebClientService

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

//        userService = spy(userService); // Create a spy for userService

        // Set up the URLs for external services (if needed)
//        ReflectionTestUtils.setField(userService, "recordServiceUrl", "http://localhost:8082");
//        ReflectionTestUtils.setField(userService, "workoutServiceUrl", "http://localhost:8083");

        MockitoAnnotations.openMocks(this);

    }

    @Test
    void createUser_WithValidRequest_UserIsSaved() {
        // Given
        UserRequest userRequest = new UserRequest("Test User", "UserCode-123", 25, 1.80, 75.0, "123456789", true, "testuser@example.com", "Build muscle");

        RecordResponse recordResponse = new RecordResponse("1", "UserCode-123", 10000.0, 0.0, 0.0, 0.0, 0.0);

        User user = User.builder()
                .userCode(userRequest.getUserCode())
                .name(userRequest.getName())
                .age(userRequest.getAge())
                .height(userRequest.getHeight())
                .weight(userRequest.getWeight())
                .phoneNr(userRequest.getPhoneNr())
                .email(userRequest.getEmail())
                .build();

        // Mock the repository and WebClientService
        when(userRepository.save(any(User.class))).thenReturn(user);
        doNothing().when(webClientService).createRecord(eq(user.getUserCode()), any(RecordResponse.class));  // Mock WebClientService's createRecord method

        // Act
        userService.createUser(userRequest);

        // Assert
        verify(userRepository, times(1)).save(any(User.class));  // Verify that save was called once
        verify(webClientService, times(1)).createRecord(eq(user.getUserCode()), any(RecordResponse.class));  // Verify that createRecord was called once
    }

//    @Test
//    void createUser_WithValidRequest_UserIsSaved2() {
//        UserRequest userRequest = new UserRequest("Test User", "UserCode-123", 25, 1.80, 75.0, "123456789", true, "testuser@example.com", "Build muscle");
//
//        RecordResponse recordResponse = new RecordResponse("1","UserCode-123", 10000.0, 0.0, 0.0, 0.0, 0.0);
//
//        // Mock WebClient behavior for POST request
////        when(webClient.post()).thenReturn(requestBodyUriSpec);
////        when(requestBodyUriSpec.uri(eq("http://localhost:8082/api/record"), any(Function.class)))
////                .thenReturn(requestBodySpec);
////
////        when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
////        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
//
//        // Mock bodyToMono(Void.class) for no response body
//        when(responseSpec.bodyToMono(Void.class)).thenReturn(Mono.empty());
//
//        // Act
//        userService.createRecord(userRequest.getUserCode(), recordResponse);  // call the method under test
//
//        // Assert - No exception means success, since it's void
//        verify(webClient).post();  // Check if the POST was made
//        verify(requestBodySpec).bodyValue(recordResponse);  // Check if bodyValue was correctly passed
//        verify(responseSpec).bodyToMono(Void.class);  // Ensure the right body typify that createRecord was called once
//    }

//    @Test
//    void createUser_WithValidRequest_UserIsSaved() {
//        // Arrange
//        UserRequest userRequest = new UserRequest("Test User", "UserCode-123", 25, 1.80, 75.0, "123456789", true, "testuser@example.com", "Build muscle");
//
//        RecordResponse recordResponse = new RecordResponse("1","UserCode-123", 10000.0, 0.0, 0.0, 0.0, 0.0);
//
////        when(webClient.post()).thenReturn(requestHeadersUriSpec); // Change to POST
////        when(requestHeadersUriSpec.uri(eq("http://localhost:8082/api/record"))).thenReturn(requestHeadersSpec); // Set the URI
////        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec); // Mock retrieve
////        when(responseSpec.bodyToMono(RecordResponse.class)).thenReturn(Mono.just(recordResponse)); // Mock the response body
//
////        when(webClient.post()).thenReturn(requestBodyUriSpec);
////        when(requestBodyUriSpec.uri(eq("http://localhost:8082/api/record"), any(Function.class))).thenReturn(requestBodySpec);
////        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
////        when(responseSpec.bodyToMono(RecordResponse.class));
//
//        // Mock WebClient and the related specs
////        when(webClient.post()).thenReturn(requestBodyUriSpec);
////        when(requestBodyUriSpec.uri(eq("http://localhost:8082/api/record"), any(Function.class))).thenReturn(requestBodySpec);
////
////        // Ensure that bodyValue() does not return null
////        when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
////
////        // Mock retrieve() and the response flow
////        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
////        when(responseSpec.bodyToMono(RecordResponse.class)).thenReturn(Mono.just(recordResponse));
//
//        when(webClient.post()).thenReturn(requestBodyUriSpec);
//        when(requestBodyUriSpec.uri(eq("http://localhost:8082/api/record"), any(Function.class))).thenReturn(requestBodySpec);
//
//// Ensure bodyValue() and retrieve() are mocked properly
//        when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
//        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
//
//// If POST returns no content, use Mono.empty() for Void response
//        when(responseSpec.bodyToMono(Void.class)).thenReturn(Mono.empty());
//
//
//        User user = User.builder()
//                .userCode(userRequest.getUserCode())
//                .name(userRequest.getName())
//                .age(userRequest.getAge())
//                .height(userRequest.getHeight())
//                .weight(userRequest.getWeight())
//                .phoneNr(userRequest.getPhoneNr())
//                .email(userRequest.getEmail())
//                .build();
//
//        when(userRepository.save(any(User.class))).thenReturn(user); // Mock saving user to repository
//        // mock the create createRecord in the usercreate function
////        when(userService.createRecord(anyString(), any(RecordResponse.class))).thenReturn(recordResponse);
//
//        // Act
//        userService.createUser(userRequest);
//
//        // Assert
//        verify(userRepository, times(1)).save(any(User.class));  // Verify that save was called once
//        verify(userService, times(1)).createRecord(eq(user.getUserCode()), eq(recordResponse));  // Verify that createRecord was called once
//    }

//    @Test
//    void createUser_WithValidRequest_UserIsSaved() {
//        // Arrange
//        UserRequest userRequest = UserRequest.builder()
//                .name("Test User")
//                .age(25)
//                .height(1.80)
//                .weight(75.0)
//                .phoneNr("123456789")
//                .male(true)
//                .email("testuser@example.com")
//                .fitnessGoals("Build muscle")
//                .build();
//
//        // Create a mock user to be saved in the repository
//        User mockUser = User.builder()
//                .userCode(userRequest.getName() + "1234")
//                .name(userRequest.getName())
//                .age(userRequest.getAge())
//                .height(userRequest.getHeight())
//                .weight(userRequest.getWeight())
//                .phoneNr(userRequest.getPhoneNr())
//                .email(userRequest.getEmail())
//                .male(userRequest.isMale())
//                .fitnessGoals(userRequest.getFitnessGoals())
//                .build();
//
//        // Mock the save behavior of the user repository
//        when(userRepository.save(any(User.class))).thenReturn(mockUser);
//
//        // Mock the WebClient call for creating a record
//        when(webClient.post()).thenReturn(requestBodyUriSpec); // Mock the POST method returning RequestBodyUriSpec
//        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec); // Mock the uri() method returning RequestBodySpec
//
//        // Ensure bodyValue() returns a mock of RequestBodySpec
//        when(requestBodySpec.bodyValue(any(RecordResponse.class))).thenReturn(requestBodySpec); // Return the same mocked RequestBodySpec
//
//        // Mock retrieve() to return ResponseSpec
//        when(requestBodySpec.retrieve()).thenReturn(responseSpec); // Mock retrieve() to return ResponseSpec
//        when(responseSpec.bodyToMono(Void.class)).thenReturn(Mono.empty()); // Simulate a successful record creation
//
//        // Act
//        userService.createUser(userRequest);
//
//        // Assert
//        verify(userRepository, times(1)).save(any(User.class)); // Verify that the user was saved
//        verify(webClient, times(1)).post(); // Verify that the WebClient POST method was called
//        verify(requestBodyUriSpec, times(1)).uri(anyString()); // Verify URI creation
//        verify(requestBodySpec, times(1)).bodyValue(any(RecordResponse.class)); // Verify the body content
//        verify(requestBodySpec, times(1)).retrieve(); // Verify that retrieve() was called
//    }
}
