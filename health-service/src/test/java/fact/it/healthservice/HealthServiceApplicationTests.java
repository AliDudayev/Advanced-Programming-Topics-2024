package fact.it.healthservice;

import fact.it.healthservice.dto.HealthRequest;
import fact.it.healthservice.repository.HealthRepository;
import fact.it.healthservice.service.HealthService;
import fact.it.healthservice.model.Health;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
class HealthServiceApplicationTests {

    @Autowired
    private HealthService healthService;
    @Autowired
    private HealthRepository healthRepository;

    @Test
    void contextLoads() {
    }

    @Test
    @Order(1)
    public void testCreateHealth() {
        HealthRequest healthRequest = HealthRequest.builder()
                .workoutCode("TestWorkout126")
                .recoveryHeartRate("60")
                .bloodPressure("120/80")
                .caloriesBurned("500")
                .oxygenSaturation("98")
                .build();
        healthService.createHealth(healthRequest);

        Health health = healthRepository.findByWorkoutCode("TestWorkout126");

        assertEquals("TestWorkout126", health.getWorkoutCode());
        assertEquals("60", health.getRecoveryHeartRate());
        assertEquals("120/80", health.getBloodPressure());
        assertEquals("500", health.getCaloriesBurned());
        assertEquals("98", health.getOxygenSaturation());
    }

    @Test
    @Order(2)
    public void testGetHealthByWorkoutCode() {
        HealthRequest healthRequest = HealthRequest.builder()
                .workoutCode("TestWorkout127")
                .recoveryHeartRate("60")
                .bloodPressure("120/80")
                .caloriesBurned("500")
                .oxygenSaturation("98")
                .build();
        healthService.createHealth(healthRequest);

        assertEquals("TestWorkout127", healthService.getHealthByWorkoutCode("TestWorkout127").getWorkoutCode());
        assertEquals("60", healthService.getHealthByWorkoutCode("TestWorkout127").getRecoveryHeartRate());
        assertEquals("120/80", healthService.getHealthByWorkoutCode("TestWorkout127").getBloodPressure());
        assertEquals("500", healthService.getHealthByWorkoutCode("TestWorkout127").getCaloriesBurned());
        assertEquals("98", healthService.getHealthByWorkoutCode("TestWorkout127").getOxygenSaturation());
        assertNull(healthService.getHealthByWorkoutCode("FakeWorkout"));
    }

    @Test
    @Order(3)
    public void testUpdateHealth() {
        HealthRequest healthRequest = HealthRequest.builder()
                .workoutCode("TestWorkout128")
                .recoveryHeartRate("60")
                .bloodPressure("120/80")
                .caloriesBurned("500")
                .oxygenSaturation("98")
                .build();
        healthService.createHealth(healthRequest);

        healthRequest = HealthRequest.builder()
                .workoutCode("TestWorkout128")
                .recoveryHeartRate("70")
                .bloodPressure("130/90")
                .caloriesBurned("600")
                .oxygenSaturation("99")
                .build();
        healthService.updateHealth("TestWorkout128", healthRequest);

        assertEquals("TestWorkout128", healthService.getHealthByWorkoutCode("TestWorkout128").getWorkoutCode());
        assertEquals("70", healthService.getHealthByWorkoutCode("TestWorkout128").getRecoveryHeartRate());
        assertEquals("130/90", healthService.getHealthByWorkoutCode("TestWorkout128").getBloodPressure());
        assertEquals("600", healthService.getHealthByWorkoutCode("TestWorkout128").getCaloriesBurned());
        assertEquals("99", healthService.getHealthByWorkoutCode("TestWorkout128").getOxygenSaturation());
    }

    @Test
    @Order(4)
    public void testGetAllHealths() {
        assertEquals("TestWorkout126", healthService.getAllHealths().get(0).getWorkoutCode());
        assertEquals("TestWorkout127", healthService.getAllHealths().get(1).getWorkoutCode());
        assertEquals("TestWorkout128", healthService.getAllHealths().get(2).getWorkoutCode());
    }


}
