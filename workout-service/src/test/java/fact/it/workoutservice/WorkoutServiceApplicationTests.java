package fact.it.workoutservice;

import fact.it.workoutservice.dto.*;
import fact.it.workoutservice.model.Workout;
import fact.it.workoutservice.repository.WorkoutRepository;
import fact.it.workoutservice.service.WorkoutService;
import org.apache.catalina.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(OrderAnnotation.class)
class WorkoutServiceApplicationTests {

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @InjectMocks
    private WorkoutService workoutService;

    @Mock
    private WorkoutRepository workoutRepository;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    private WebClient.RequestBodySpec requestBodySpec;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(workoutService, "healthServiceBaseUrl", "http://localhost:8084");
        ReflectionTestUtils.setField(workoutService, "recordServiceBaseUrl", "http://localhost:8082");
        ReflectionTestUtils.setField(workoutService, "userServiceBaseUrl", "http://localhost:8081");
    }

    @Test
//    @Order(1)
    public void createWorkout_WithValidRequest_WorkoutIsSaved() {
        // Arrange
        WorkoutRequest workoutRequest = WorkoutRequest.builder()
                .name("TestWorkout")
                .userCode("TestUser126")
                .workoutCode("TestWorkout126")
                .duration("60")
                .sets("3")
                .reps("10")
                .pauseBetweenReps("60")
                .type("Strength")
                .weight("50.0")
                .distance("200.0")
                .speed("50.0")
                .description("Test description")
                .build();

        Workout workout = Workout.builder()
                .name("TestWorkout")
                .userCode("TestUser126")
                .workoutCode("TestWorkout126")
                .duration("60")
                .sets("3")
                .reps("10")
                .pauseBetweenReps("60")
                .type("Strength")
                .weight("50.0")
                .distance("110.0")
                .speed("10.0")
                .description("Test description")
                .build();

        // Create a record responce
        RecordResponse recordResponse = RecordResponse.builder()
                .userCode("TestUser126")
                .fastestTime(1000.0)
                .longestDistance(100.0)
                .maxWeightLifted(70.0)
                .longestWorkoutDuration(150.0)
                .mostCaloriesBurned(600.0)
                .build();

        UserResponse userResponse = UserResponse.builder()
                .userCode("TestUser126")
                .weight(80.0)
                .build();

        when(workoutRepository.save(any(Workout.class))).thenReturn(workout);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(RecordResponse.class)).thenReturn(Mono.just(recordResponse));

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(UserResponse.class)).thenReturn(Mono.just(userResponse));

        when(webClient.put()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any(RecordResponse.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Void.class)).thenReturn(Mono.empty());

        // Act
        // Capture arguments
        ArgumentCaptor<RecordResponse> recordCaptor = ArgumentCaptor.forClass(RecordResponse.class);
        ArgumentCaptor<String> uriCaptor = ArgumentCaptor.forClass(String.class);
        workoutService.createWorkout(workoutRequest);


        // Assert
        verify(workoutRepository, times(2)).save(any(Workout.class));

        // Verify RecordResponse update
        verify(requestBodySpec, times(2)).bodyValue(recordCaptor.capture());
        RecordResponse capturedRecord = recordCaptor.getValue();
        assertNotNull(capturedRecord);
        assertEquals("TestUser126", capturedRecord.getUserCode());
        assertEquals(4.0, capturedRecord.getFastestTime(), 0.01); // 5.0 km / 10.0 km/h
        assertEquals(200.0, capturedRecord.getLongestDistance(), 0.01);
        assertEquals(70.0, capturedRecord.getMaxWeightLifted(), 0.01);
        assertEquals(150.0, capturedRecord.getLongestWorkoutDuration(), 0.01);
        assertEquals(1024.0, capturedRecord.getMostCaloriesBurned(), 0.01); // MET calculation

        // Verify UserResponse call
        verify(requestHeadersUriSpec, times(4)).uri(uriCaptor.capture()); // Once for Record and once for User
        List<String> capturedUris = uriCaptor.getAllValues();
        assertTrue(capturedUris.get(1).contains("userCode=TestUser126"));

        // Validate that the mocked UserResponse was used
        assertEquals(80.0, userResponse.getWeight(), 0.01);
    }

    @Test
//    @Order(2)
    public void createWorkout_WithNullDistanceAndSpeed_SkipsFastestTimeCalculation() {
        // Arrange
        WorkoutRequest workoutRequest = WorkoutRequest.builder()
                .name("TestWorkout")
                .userCode("TestUser126")
                .workoutCode("TestWorkout126")
                .duration("60")
                .weight("50.0")
                .distance(null)
                .speed(null)
                .build();

        Workout workout = Workout.builder()
                .name("TestWorkout")
                .userCode("TestUser126")
                .workoutCode("TestWorkout126")
                .duration(null)
                .sets(null)
                .reps(null)
                .pauseBetweenReps("60")
                .type("Strength")
                .weight(null)
                .distance(null)
                .speed(null)
                .description("Test description")
                .build();

        // Create a record responce
        RecordResponse recordResponse = RecordResponse.builder()
                .userCode("TestUser126")
                .fastestTime(1000.0)
                .longestDistance(100.0)
                .maxWeightLifted(70.0)
                .longestWorkoutDuration(0.0)
                .mostCaloriesBurned(0.0)
                .build();

        UserResponse userResponse = UserResponse.builder()
                .userCode("TestUser126")
                .weight(80.0)
                .build();

        when(workoutRepository.save(any(Workout.class))).thenReturn(workout);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(RecordResponse.class)).thenReturn(Mono.just(recordResponse));

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(UserResponse.class)).thenReturn(Mono.just(userResponse));

        when(webClient.put()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any(RecordResponse.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Void.class)).thenReturn(Mono.empty());

        // Act
        // Capture arguments
        ArgumentCaptor<RecordResponse> recordCaptor = ArgumentCaptor.forClass(RecordResponse.class);
        ArgumentCaptor<String> uriCaptor = ArgumentCaptor.forClass(String.class);
        workoutService.createWorkout(workoutRequest);



        // Assert
        verify(workoutRepository, times(2)).save(any(Workout.class));

        // Verify RecordResponse update
        verify(requestBodySpec, times(2)).bodyValue(recordCaptor.capture());
        RecordResponse capturedRecord = recordCaptor.getValue();
        assertNotNull(capturedRecord);
        assertEquals("TestUser126", capturedRecord.getUserCode());
        assertEquals(1000.0, capturedRecord.getFastestTime(), 0.01); // 5.0 km / 10.0 km/h
        assertEquals(200.0, capturedRecord.getLongestDistance(), 0.01);
        assertEquals(70.0, capturedRecord.getMaxWeightLifted(), 0.01);
        assertEquals(150.0, capturedRecord.getLongestWorkoutDuration(), 0.01);
        assertEquals(1024.0, capturedRecord.getMostCaloriesBurned(), 0.01); // MET calculation

        // Verify UserResponse call
        verify(requestHeadersUriSpec, times(4)).uri(uriCaptor.capture()); // Once for Record and once for User
        List<String> capturedUris = uriCaptor.getAllValues();
        assertTrue(capturedUris.get(1).contains("userCode=TestUser126"));

        // Validate that the mocked UserResponse was used
        assertEquals(80.0, userResponse.getWeight(), 0.01);
    }

    @Test
//    @Order(3)
    public void createWorkout_WithSpeedBelow8_MetCalculationIs6() {
        // Arrange
        WorkoutRequest workoutRequest = WorkoutRequest.builder()
                .name("CardioWorkout")
                .userCode("UserXYZ")
                .workoutCode("Cardio789")
                .duration("30")
                .sets("3")
                .reps("15")
                .pauseBetweenReps("20")
                .type("Cardio")
                .weight("45.0")
                .distance(null)
                .speed(null)
                .description("Endurance training")
                .build();

        Workout workout = Workout.builder()
                .name("CardioWorkout")
                .userCode("UserXYZ")
                .workoutCode("Cardio789")
                .duration("30")
                .sets("3")
                .reps("15")
                .pauseBetweenReps("20")
                .type("Cardio")
                .weight("45.0")
                .distance(null)
                .speed(null)
                .description("Endurance training")
                .build();

        // Create a record responce
        RecordResponse recordResponse = RecordResponse.builder()
                .userCode("UserXYZ")
                .fastestTime(800.0)
                .longestDistance(300.0)
                .maxWeightLifted(60.0)
                .longestWorkoutDuration(90.0)
                .mostCaloriesBurned(500.0)
                .build();

        UserResponse userResponse = UserResponse.builder()
                .userCode("TestUser126")
                .weight(70.0)
                .build();

        when(workoutRepository.save(any(Workout.class))).thenReturn(workout);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(RecordResponse.class)).thenReturn(Mono.just(recordResponse));

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(UserResponse.class)).thenReturn(Mono.just(userResponse));

        when(webClient.put()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any(RecordResponse.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Void.class)).thenReturn(Mono.empty());

        // Act
        // Capture arguments
        ArgumentCaptor<RecordResponse> recordCaptor = ArgumentCaptor.forClass(RecordResponse.class);
        ArgumentCaptor<String> uriCaptor = ArgumentCaptor.forClass(String.class);
        workoutService.createWorkout(workoutRequest);

        // Act
        workoutService.createWorkout(workoutRequest);

        // Assert
        verify(workoutRepository, times(2)).save(any(Workout.class));

        // Verify RecordResponse update
        verify(requestBodySpec, times(2)).bodyValue(recordCaptor.capture());
        RecordResponse capturedRecord = recordCaptor.getValue();
        assertNotNull(capturedRecord);
        assertEquals("UserXYZ", capturedRecord.getUserCode());
        assertEquals(1000.0, capturedRecord.getFastestTime(), 0.01); // 5.0 km / 10.0 km/h
        assertEquals(200.0, capturedRecord.getLongestDistance(), 0.01);
        assertEquals(70.0, capturedRecord.getMaxWeightLifted(), 0.01);
        assertEquals(150.0, capturedRecord.getLongestWorkoutDuration(), 0.01);
        assertEquals(1024.0, capturedRecord.getMostCaloriesBurned(), 0.01); // MET calculation

        // Verify UserResponse call
        verify(requestHeadersUriSpec, times(4)).uri(uriCaptor.capture()); // Once for Record and once for User
        List<String> capturedUris = uriCaptor.getAllValues();
        assertTrue(capturedUris.get(1).contains("userCode=TestUser126"));

        // Validate that the mocked UserResponse was used
        assertEquals(80.0, userResponse.getWeight(), 0.01);
    }

    @Test
//    @Order(4)
    public void testGetHealthData() {
        // Arrange
        HealthResponse healthResponse = HealthResponse.builder()
                .workoutCode("TestWorkoutCode")
                .recoveryHeartRate("60")
                .bloodPressure("120/80")
                .caloriesBurned("500")
                .oxygenSaturation("98")
                .build();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(HealthResponse.class)).thenReturn(Mono.just(healthResponse));

        //act
        HealthResponse healthResult = workoutService.getHealthData(healthResponse.getWorkoutCode());

        // Assert
        assertNotNull(healthResult);
        assertEquals("TestWorkoutCode", healthResult.getWorkoutCode());
        assertEquals("60", healthResult.getRecoveryHeartRate());
        assertEquals("120/80", healthResult.getBloodPressure());
        assertEquals("500", healthResult.getCaloriesBurned());
        assertEquals("98", healthResult.getOxygenSaturation());
    }

    @Test
//    @Order(3)
    void testGetAllWorkouts_Success() {
        // Arrange
        Workout workout1 = Workout.builder()
                .name("Workout1")
                .userCode("TestUser")
                .workoutCode("WorkoutCode123")
                .build();

        Workout workout2 = Workout.builder()
                .name("Workout2")
                .userCode("TestUser")
                .workoutCode("WorkoutCode456")
                .build();


        when(workoutRepository.findAll()).thenReturn(List.of(workout1, workout2));

        // Act
        List<WorkoutResponse> workouts = workoutService.getAllWorkouts();

        // Assert
        assertEquals(2, workouts.size());
        assertEquals("Workout1", workouts.get(0).getName());
        assertEquals("Workout2", workouts.get(1).getName());
        assertEquals("WorkoutCode123", workouts.get(0).getWorkoutCode());
        assertEquals("WorkoutCode456", workouts.get(1).getWorkoutCode());
        verify(workoutRepository, times(1)).findAll();
    }

    @Test
//    @Order(4)
    void testGetWorkoutByWorkoutCode_Success() {
        // Arrange
        Workout workout = Workout.builder()
                .name("TestWorkout")
                .userCode("TestUser")
                .workoutCode("WorkoutCode123")
                .build();

        when(workoutRepository.findByWorkoutCode("WorkoutCode123")).thenReturn(workout);

        // Act
        WorkoutResponse response = workoutService.getWorkoutByWorkoutCode("WorkoutCode123");

        // Assert
        assertNotNull(response);
        assertEquals("TestWorkout", response.getName());
        assertEquals("WorkoutCode123", response.getWorkoutCode());
        verify(workoutRepository, times(1)).findByWorkoutCode("WorkoutCode123");
    }

    @Test
//    @Order(4)
    void testGetWorkoutByWorkoutCode_NoSuccess() {
        // Arrange
        Workout workout = Workout.builder()
                .name("TestWorkout")
                .userCode("TestUser")
                .workoutCode("WorkoutCode123")
                .build();

        when(workoutRepository.findByWorkoutCode(workout.getWorkoutCode())).thenReturn(workout);

        // Act
        WorkoutResponse response = workoutService.getWorkoutByWorkoutCode("WorkoutCode123");

        // Assert
        assertNotNull(response);
        assertEquals("TestWorkout", response.getName());
        assertEquals("WorkoutCode123", response.getWorkoutCode());
        verify(workoutRepository, times(1)).findByWorkoutCode("WorkoutCode123");
    }

    @Test
//    @Order(6)
    void testGetWorkoutByUserCode_Success() {
        // Arrange
        Workout workout = Workout.builder()
                .name("TestWorkout")
                .userCode("TestUser")
                .workoutCode("WorkoutCode123")
                .build();

        when(workoutRepository.findByUserCode("TestUser")).thenReturn(List.of(workout));

        // Act
        List<WorkoutResponse> responses = workoutService.getWorkoutByUserCode("TestUser");

        // Assert
        assertEquals(1, responses.size());
        assertEquals("TestWorkout", responses.get(0).getName());
        verify(workoutRepository, times(1)).findByUserCode("TestUser");
    }

    @Test
//    @Order(7)
    void testGetWorkoutByUserCode_NoWorkouts() {
        // Arrange
        when(workoutRepository.findByUserCode("TestUser")).thenReturn(Collections.emptyList());

        // Act
        List<WorkoutResponse> responses = workoutService.getWorkoutByUserCode("TestUser");

        // Assert
        assertTrue(responses.isEmpty());
        verify(workoutRepository, times(1)).findByUserCode("TestUser");
    }

}
