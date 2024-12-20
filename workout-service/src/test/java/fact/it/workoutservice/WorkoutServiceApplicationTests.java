package fact.it.workoutservice;

import fact.it.workoutservice.dto.*;
import fact.it.workoutservice.model.Workout;
import fact.it.workoutservice.repository.WorkoutRepository;
import fact.it.workoutservice.service.WorkoutService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
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
    @Order(1)
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
        verify(workoutRepository, times(1)).save(any(Workout.class));

        // Verify RecordResponse update
        verify(requestBodySpec, times(1)).bodyValue(recordCaptor.capture());
        RecordResponse capturedRecord = recordCaptor.getValue();
        assertNotNull(capturedRecord);
        assertEquals("TestUser126", capturedRecord.getUserCode());
        assertEquals(4.0, capturedRecord.getFastestTime(), 0.01); // 5.0 km / 10.0 km/h
        assertEquals(200.0, capturedRecord.getLongestDistance(), 0.01);
        assertEquals(70.0, capturedRecord.getMaxWeightLifted(), 0.01);
        assertEquals(150.0, capturedRecord.getLongestWorkoutDuration(), 0.01);
        assertEquals(1024.0, capturedRecord.getMostCaloriesBurned(), 0.01); // MET calculation

        // Verify UserResponse call
        verify(requestHeadersUriSpec, times(2)).uri(uriCaptor.capture()); // Once for Record and once for User

        // Validate that the mocked UserResponse was used
        assertEquals(80.0, userResponse.getWeight(), 0.01);
    }

    @Test
    @Order(2)
    public void createWorkout_Speed9Point5_Success() {
        // Arrange
        WorkoutRequest request = new WorkoutRequest();
        request.setName("Test Workout");
        request.setUserCode("User123");
        request.setWorkoutCode("WORKOUT123");
        request.setDuration("60");
        request.setSets("3");
        request.setReps("12");
        request.setPauseBetweenReps("30");
        request.setType("Strength");
        request.setWeight("50");
        request.setDistance("5");
        request.setSpeed("9.5");
        request.setDescription("Sample workout description");

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
                .distance("80.0")
                .speed("9.5")
                .description("Test description")
                .build();

        RecordResponse recordResponse = new RecordResponse("User123", 100.0, 200.0, 150.0, 100.0, 1000.0);
        UserResponse userResponse = new UserResponse("User123", 70.0);

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
        workoutService.createWorkout(request);

        // Assert
        verify(workoutRepository, times(1)).save(any(Workout.class));

        // Verify RecordResponse update
        verify(requestBodySpec, times(1)).bodyValue(recordCaptor.capture());
        RecordResponse capturedRecord = recordCaptor.getValue();
        assertNotNull(capturedRecord);
        assertEquals("User123", capturedRecord.getUserCode());
    }

    @Test
    @Order(3)
    public void createWorkout_Speed12WeightNullFastestTime0_Success() {
        // Arrange
        WorkoutRequest request = new WorkoutRequest();
        request.setName("Test Workout");
        request.setUserCode("User123");
        request.setWorkoutCode("WORKOUT123");
        request.setDuration("60");
        request.setSets("3");
        request.setReps("12");
        request.setPauseBetweenReps("30");
        request.setType("Strength");
        request.setWeight(null);
        request.setDistance("5");
        request.setSpeed("12.0");
        request.setDescription("Sample workout description");

        Workout workout = Workout.builder()
                .name("TestWorkout")
                .userCode("TestUser126")
                .workoutCode("TestWorkout126")
                .duration("60")
                .sets("3")
                .reps("10")
                .pauseBetweenReps("60")
                .type("Strength")
                .weight(null)
                .distance("110.0")
                .speed("12.0")
                .description("Test description")
                .build();

        RecordResponse recordResponse = new RecordResponse("User123", 0.0, 3.0, 40.0, 50.0, 400.0);
        UserResponse userResponse = new UserResponse("User123", 70.0);

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
        workoutService.createWorkout(request);

        // Assert
        verify(workoutRepository, times(1)).save(any(Workout.class));

        // Verify RecordResponse update
        verify(requestBodySpec, times(1)).bodyValue(recordCaptor.capture());
        RecordResponse capturedRecord = recordCaptor.getValue();
        assertNotNull(capturedRecord);
        assertEquals("User123", capturedRecord.getUserCode());
    }

    @Test
    @Order(4)
    public void createWorkout_Speed5_Success() {
        // Arrange
        WorkoutRequest request = new WorkoutRequest();
        request.setName("Test Workout");
        request.setUserCode("User123");
        request.setWorkoutCode("WORKOUT123");
        request.setDuration("60");
        request.setSets("3");
        request.setReps("12");
        request.setPauseBetweenReps("30");
        request.setType("Strength");
        request.setWeight("500.0");
        request.setDistance("5");
        request.setSpeed("5.0");
        request.setDescription("Sample workout description");

        Workout workout = Workout.builder()
                .name("TestWorkout")
                .userCode("TestUser126")
                .workoutCode("TestWorkout126")
                .duration("60")
                .sets("3")
                .reps("10")
                .pauseBetweenReps("60")
                .type("Strength")
                .weight("500.0")
                .distance("110.0")
                .speed("5.0")
                .description("Test description")
                .build();

        RecordResponse recordResponse = new RecordResponse("User123", 1000.0, 3.0, 40.0, 50.0, 400.0);
        UserResponse userResponse = new UserResponse("User123", 70.0);

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
        workoutService.createWorkout(request);

        // Assert
        verify(workoutRepository, times(1)).save(any(Workout.class));

        // Verify RecordResponse update
        verify(requestBodySpec, times(1)).bodyValue(recordCaptor.capture());
        RecordResponse capturedRecord = recordCaptor.getValue();
        assertNotNull(capturedRecord);
        assertEquals("User123", capturedRecord.getUserCode());
    }

    @Test
    @Order(5)
    public void createWorkout_SpeedNull_Success() {
        // Arrange
        WorkoutRequest request = new WorkoutRequest();
        request.setName("Test Workout");
        request.setUserCode("User123");
        request.setWorkoutCode("WORKOUT123");
        request.setDuration("60");
        request.setSets("3");
        request.setReps("12");
        request.setPauseBetweenReps("30");
        request.setType("Strength");
        request.setWeight("500.0");
        request.setDistance("5");
        request.setSpeed(null);
        request.setDescription("Sample workout description");

        Workout workout = Workout.builder()
                .name("TestWorkout")
                .userCode("TestUser126")
                .workoutCode("TestWorkout126")
                .duration("60")
                .sets("3")
                .reps("10")
                .pauseBetweenReps("60")
                .type("Strength")
                .weight("500.0")
                .distance("110.0")
                .speed(null)
                .description("Test description")
                .build();

        RecordResponse recordResponse = new RecordResponse("User123", 100.0, 3.0, 40.0, 50.0, 400.0);

        when(workoutRepository.save(any(Workout.class))).thenReturn(workout);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(RecordResponse.class)).thenReturn(Mono.just(recordResponse));

        when(webClient.put()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any(RecordResponse.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Void.class)).thenReturn(Mono.empty());

        // Act
        // Capture arguments
        ArgumentCaptor<RecordResponse> recordCaptor = ArgumentCaptor.forClass(RecordResponse.class);
        workoutService.createWorkout(request);


        // Assert
        verify(workoutRepository, times(1)).save(any(Workout.class));

        // Verify RecordResponse update
        verify(requestBodySpec, times(1)).bodyValue(recordCaptor.capture());
        RecordResponse capturedRecord = recordCaptor.getValue();
        assertNotNull(capturedRecord);
        assertEquals("User123", capturedRecord.getUserCode());
    }

    @Test
    @Order(6)
    public void createWorkout_DurationNullWeightNullFastestTimeNull_Success() {
        // Arrange
        WorkoutRequest request = new WorkoutRequest();
        request.setName("Test Workout");
        request.setUserCode("User123");
        request.setWorkoutCode("WORKOUT123");
        request.setDuration(null);
        request.setSets("3");
        request.setReps("12");
        request.setPauseBetweenReps("30");
        request.setType("Strength");
        request.setWeight(null);
        request.setDistance("5");
        request.setSpeed("12.0");
        request.setDescription("Sample workout description");

        Workout workout = Workout.builder()
                .name("TestWorkout")
                .userCode("TestUser126")
                .workoutCode("TestWorkout126")
                .duration(null)
                .sets("3")
                .reps("10")
                .pauseBetweenReps("60")
                .type("Strength")
                .weight(null)
                .distance("110.0")
                .speed("12.0")
                .description("Test description")
                .build();

        RecordResponse recordResponse = new RecordResponse("User123", null, 3.0, 40.0, 50.0, 400.0);

        when(workoutRepository.save(any(Workout.class))).thenReturn(workout);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(RecordResponse.class)).thenReturn(Mono.just(recordResponse));

        when(webClient.put()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any(RecordResponse.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Void.class)).thenReturn(Mono.empty());

        // Act
        // Capture arguments
        ArgumentCaptor<RecordResponse> recordCaptor = ArgumentCaptor.forClass(RecordResponse.class);
        workoutService.createWorkout(request);

        // Assert
        verify(workoutRepository, times(1)).save(any(Workout.class));

        // Verify RecordResponse update
        verify(requestBodySpec, times(1)).bodyValue(recordCaptor.capture());
        RecordResponse capturedRecord = recordCaptor.getValue();
        assertNotNull(capturedRecord);
        assertEquals("User123", capturedRecord.getUserCode());
    }

    @Test
    @Order(7)
    public void createWorkout_MissingSpeedAndDistanceRequest_Success() {
        // Arrange
        WorkoutRequest request = new WorkoutRequest();
        request.setName("Test Workout");
        request.setUserCode("User123");
        request.setWorkoutCode("WORKOUT123");
        request.setDuration("60");
        request.setSets("3");
        request.setReps("12");
        request.setPauseBetweenReps("30");
        request.setType("Strength");
        request.setWeight("50");
        request.setDistance(null);
        request.setSpeed(null);
        request.setDescription("Sample workout description");

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

        RecordResponse recordResponse = new RecordResponse("User123", 100.0, 3.0, 40.0, 50.0, 400.0);

        when(workoutRepository.save(any(Workout.class))).thenReturn(workout);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(RecordResponse.class)).thenReturn(Mono.just(recordResponse));

        when(webClient.put()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any(RecordResponse.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Void.class)).thenReturn(Mono.empty());

        // Act
        // Capture arguments
        workoutService.createWorkout(request);

        // Assert
        verify(workoutRepository, times(1)).save(any(Workout.class));

        // Verify RecordResponse update
        verify(workoutRepository, times(1)).save(any(Workout.class));
        verify(webClient, times(1)).get();
    }

    @Test
    @Order(8)
    public void createWorkout_WithNullDistanceAndSpeedWorkout_SkipsFastestTimeCalculation() {
        // Arrange
        WorkoutRequest request = new WorkoutRequest();
        request.setName("Test Workout");
        request.setUserCode("User123");
        request.setWorkoutCode("WORKOUT123");
        request.setDuration("60");
        request.setSets("3");
        request.setReps("12");
        request.setPauseBetweenReps("30");
        request.setType("Strength");
        request.setWeight("50");
        request.setDistance("5");
        request.setSpeed("10");
        request.setDescription("Sample workout description");

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
                .distance("200")
                .speed("10.0")
                .description("Test description")
                .build();

        RecordResponse recordResponse = new RecordResponse("User123", 100.0, 3.0, 40.0, 50.0, 400.0);

        UserResponse userResponse = new UserResponse("User123", 70.0);

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
        assertDoesNotThrow(() -> workoutService.createWorkout(request));

        // Assert
        verify(workoutRepository, times(1)).save(any(Workout.class));
        verify(webClient, times(2)).get();
        verify(webClient, times(1)).put();
    }

    @Test
    @Order(9)
    public void getHealthData_ValidData_Success() {
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
    @Order(10)
    void getAllWorkouts_ValidData_Success() {
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
    @Order(11)
    void getWorkoutByWorkoutCode_ValidData_Success() {
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
    @Order(12)
    void getWorkoutByUserCode_Workouts_Success() {
        // Arrange
        Workout workout1 = Workout.builder()
                .name("TestWorkout1")
                .userCode("TestUser1")
                .workoutCode("WorkoutCode123")
                .build();
        Workout workout2 = Workout.builder()
                .name("TestWorkout2")
                .userCode("TestUser2")
                .workoutCode("WorkoutCode456")
                .build();

        when(workoutRepository.findByUserCode("TestUser")).thenReturn(List.of(workout1, workout2));

        // Act
        List<WorkoutResponse> responses = workoutService.getWorkoutByUserCode("TestUser");

        // Assert
        assertEquals(2, responses.size());
        assertEquals("TestWorkout1", responses.get(0).getName());
        assertEquals("TestWorkout2", responses.get(1).getName());
        verify(workoutRepository, times(1)).findByUserCode("TestUser");
    }

    @Test
    @Order(13)
    void getWorkoutByUserCode_NoWorkouts_Success() {
        // Arrange
        when(workoutRepository.findByUserCode("TestUser")).thenReturn(Collections.emptyList());

        // Act
        List<WorkoutResponse> responses = workoutService.getWorkoutByUserCode("TestUser");

        // Assert
        assertTrue(responses.isEmpty());
        verify(workoutRepository, times(1)).findByUserCode("TestUser");
    }

}
