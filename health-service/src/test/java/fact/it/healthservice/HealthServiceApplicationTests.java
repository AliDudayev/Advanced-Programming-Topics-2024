package fact.it.healthservice;

import fact.it.healthservice.dto.HealthRequest;
import fact.it.healthservice.dto.HealthResponse;
import fact.it.healthservice.repository.HealthRepository;
import fact.it.healthservice.service.HealthService;
import fact.it.healthservice.model.Health;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@TestMethodOrder(OrderAnnotation.class)
class HealthServiceApplicationTests {

    @InjectMocks
    private HealthService healthService;

    @Mock
    private HealthRepository healthRepository;

    @Test
    @Order(1)
    void createHealth_NewHealth_SavesSuccessfully() {
        // Arrange
        HealthRequest healthRequest = HealthRequest.builder()
                .workoutCode("TestWorkout126")
                .recoveryHeartRate("60")
                .bloodPressure("120/80")
                .caloriesBurned("500")
                .oxygenSaturation("98")
                .build();

        when(healthRepository.findByWorkoutCode(anyString())).thenReturn(null);

        // Act
        healthService.createHealth(healthRequest);

        // Assert
        verify(healthRepository, times(1)).save(any(Health.class));
    }

    @Test
    @Order(2)
    void createHealth_HealthAlreadyExists_DoesNotSaveAgain() {
        // Arrange
        HealthRequest healthRequest = HealthRequest.builder()
                .workoutCode("TestWorkout126")
                .recoveryHeartRate("60")
                .bloodPressure("120/80")
                .caloriesBurned("500")
                .oxygenSaturation("98")
                .build();

        Health health = Health.builder()
                .workoutCode("TestWorkout126")
                .recoveryHeartRate("80")
                .bloodPressure("130/85")
                .caloriesBurned("600")
                .oxygenSaturation("100")
                .build();

        when(healthRepository.findByWorkoutCode(anyString())).thenReturn(health);

        // Act
        healthService.createHealth(healthRequest);

        // Assert
        verify(healthRepository, never()).save(any(Health.class));
    }

    @Test
    @Order(3)
    void getAllHealths_ReturnsListOfHealthRecords() {
        // Arrange
        Health health1 = Health.builder().workoutCode("TestWorkout126")
                .recoveryHeartRate("60")
                .bloodPressure("120/80")
                .caloriesBurned("500")
                .oxygenSaturation("98")
                .build();
        Health health2 = Health.builder().workoutCode("TestWorkout456")
                .recoveryHeartRate("60")
                .bloodPressure("120/80")
                .caloriesBurned("500")
                .oxygenSaturation("98")
                .build();

        when(healthRepository.findAll()).thenReturn(Arrays.asList(health1, health2));

        // Act
        List<HealthResponse> result = healthService.getAllHealths();

        // Assert
        assertEquals(2, result.size());
        assertEquals("TestWorkout126", result.get(0).getWorkoutCode());
        assertEquals("TestWorkout456", result.get(1).getWorkoutCode());
    }

    @Test
    @Order(4)
    void getHealthByWorkoutCode_ExistingCode_ReturnsHealthResponse() {
        // Arrange
        Health health = Health.builder()
                .workoutCode("TestWorkout126")
                .recoveryHeartRate("60")
                .bloodPressure("120/80")
                .caloriesBurned("500")
                .oxygenSaturation("98")
                .build();

        when(healthRepository.findByWorkoutCode("TestWorkout126")).thenReturn(health);

        // Act
        HealthResponse result = healthService.getHealthByWorkoutCode("TestWorkout126");

        // Assert
        assertNotNull(result);
        assertEquals("TestWorkout126", result.getWorkoutCode());
        assertEquals("60", result.getRecoveryHeartRate());
        assertEquals("120/80", result.getBloodPressure());
        assertEquals("500", result.getCaloriesBurned());
        assertEquals("98", result.getOxygenSaturation());
    }

    @Test
    @Order(5)
    void updateHealth_ExistingHealthRecord_UpdatesSuccessfully() {
        // Arrange
        Health oldHealth = Health.builder()
                .workoutCode("TestWorkout128")
                .recoveryHeartRate("60")
                .bloodPressure("120/80")
                .caloriesBurned("500")
                .oxygenSaturation("98")
                .build();
        HealthRequest newHealthRequest = HealthRequest.builder()
                .recoveryHeartRate("70")
                .bloodPressure("130/90")
                .caloriesBurned("600")
                .oxygenSaturation("99")
                .build();

        when(healthRepository.findByWorkoutCode("TestWorkout128")).thenReturn(oldHealth);

        // Act
        healthService.updateHealth("TestWorkout128", newHealthRequest);

        // Assert
        verify(healthRepository, times(1)).save(oldHealth);
        assertEquals("70", oldHealth.getRecoveryHeartRate());
        assertEquals("130/90", oldHealth.getBloodPressure());
        assertEquals("600", oldHealth.getCaloriesBurned());
        assertEquals("99", oldHealth.getOxygenSaturation());
    }

    @Test
    @Order(6)
    void updateHealth_NotExistingHealthRecord_DoesNotUpdate() {
        // Arrange
        HealthRequest health = HealthRequest.builder()
                .recoveryHeartRate("70")
                .bloodPressure("130/90")
                .caloriesBurned("600")
                .oxygenSaturation("99")
                .build();

        when(healthRepository.findByWorkoutCode("TestWorkout128")).thenReturn(null);

        // Act
        healthService.updateHealth("TestWorkout128", health);

        // Assert
        verify(healthRepository, times(0)).save(any());
    }
}
