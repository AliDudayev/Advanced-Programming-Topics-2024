package fact.it.workoutservice;

import fact.it.workoutservice.dto.HealthResponse;
import fact.it.workoutservice.dto.WorkoutRequest;
import fact.it.workoutservice.dto.WorkoutResponse;
import fact.it.workoutservice.model.Workout;
import fact.it.workoutservice.repository.WorkoutRepository;
import fact.it.workoutservice.service.WorkoutService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
class WorkoutServiceApplicationTests {

    @Autowired
    private WorkoutService workoutService;
    @Autowired
    private WorkoutRepository workoutRepository;

    @Test
    void contextLoads() {
    }

    @Test
    @Order(1)
    public void testCreateWorkout() {
        LocalDate now = LocalDate.now();
        WorkoutRequest workoutRequest = WorkoutRequest.builder()
                .name("TestWorkout")
                .userCode("TestUser126")
                .workoutCode("TestWorkout126")
                .date(now)
                .duration("60")
                .sets("3")
                .reps("10")
                .pauseBetweenReps("60")
                .type("Strength")
                .weight("50.0")
                .distance("0.0")
                .speed("0.0")
                .description("Test description")
                .build();
        workoutService.createWorkout(workoutRequest);

        Workout workout = workoutRepository.findByWorkoutCode("TestWorkout126");

        assertEquals("TestWorkout", workout.getName());
        assertEquals("TestUser126", workout.getUserCode());
        assertEquals("TestWorkout126", workout.getWorkoutCode());
        assertEquals(now, workout.getDate());
        assertEquals("60", workout.getDuration());
        assertEquals("3", workout.getSets());
        assertEquals("10", workout.getReps());
        assertEquals("60", workout.getPauseBetweenReps());
        assertEquals("Strength", workout.getType());
        assertEquals("50.0", workout.getWeight());
        assertEquals("0.0", workout.getDistance());
        assertEquals("0.0", workout.getSpeed());
        assertEquals("Test description", workout.getDescription());
    }

    @Test
    @Order(2)
    public void testGetAllWorkouts() {
        LocalDate now = LocalDate.now();

        WorkoutRequest workoutRequest = WorkoutRequest.builder()
                .name("TestWorkout")
                .userCode("TestUser127")
                .workoutCode("TestWorkout127")
                .date(now)
                .duration("60")
                .sets("3")
                .reps("10")
                .pauseBetweenReps("60")
                .type("Strength")
                .weight("50.0")
                .distance("0.0")
                .speed("0.0")
                .description("Test description")
                .build();
        workoutService.createWorkout(workoutRequest);
        assertEquals("TestWorkout126", workoutService.getAllWorkouts().get(workoutService.getAllWorkouts().size() - 1).getWorkoutCode());
        assertEquals("TestWorkout127", workoutService.getAllWorkouts().get(workoutService.getAllWorkouts().size() - 1).getWorkoutCode());
    }

    @Test
    @Order(3)
    public void testGetWorkoutByWorkoutCode() {
        LocalDate now = LocalDate.now();
        WorkoutRequest workoutRequest = WorkoutRequest.builder()
                .name("TestWorkout")
                .userCode("TestUser128")
                .workoutCode("TestWorkout128")
                .date(now)
                .duration("60")
                .sets("3")
                .reps("10")
                .pauseBetweenReps("60")
                .type("Strength")
                .weight("50.0")
                .distance("0.0")
                .speed("0.0")
                .description("Test description")
                .build();
        workoutService.createWorkout(workoutRequest);

        assertEquals("TestWorkout128", workoutService.getWorkoutByWorkoutCode("TestWorkout128").getWorkoutCode());
        assertEquals("TestWorkout", workoutService.getWorkoutByWorkoutCode("TestWorkout128").getName());
        assertEquals("TestUser128", workoutService.getWorkoutByWorkoutCode("TestWorkout128").getUserCode());
        assertEquals(now, workoutService.getWorkoutByWorkoutCode("TestWorkout128").getDate());
        assertNull(workoutService.getWorkoutByWorkoutCode("FakeWorkout"));
    }

//    @Test
//    @Order(4)
//    public void testGetHealthData() {
//        LocalDate now = LocalDate.now();
//        WorkoutRequest workoutRequest = WorkoutRequest.builder()
//                .name("TestWorkout")
//                .userCode("TestUser129")
//                .workoutCode("TestWorkout129")
//                .date(now)
//                .duration("60")
//                .sets("3")
//                .reps("10")
//                .pauseBetweenReps("60")
//                .type("Strength")
//                .weight("50.0")
//                .distance("0.0")
//                .speed("0.0")
//                .description("Test description")
//                .build();
//        workoutService.createWorkout(workoutRequest);
//
//        HealthResponse healthResponse = workoutService.getHealthData("TestWorkout129");
//
//        assertNull(healthResponse);
//    }

//
//    @Test
//    @Order(4)
//    public void testMapToWorkoutResponse() {
//        LocalDate now = LocalDate.now();
//        Workout workout = Workout.builder()
//                .id(1L)
//                .name("TestWorkout")
//                .userCode("TestUser129")
//                .workoutCode("TestWorkout129")
//                .date(now)
//                .duration("60")
//                .sets("3")
//                .reps("10")
//                .pauseBetweenReps("60")
//                .type("Strength")
//                .weight("50.0")
//                .distance("0.0")
//                .speed("0.0")
//                .description("Test description")
//                .build();
//        WorkoutResponse workoutResponse = workoutService.mapToWorkoutResponse(workout);
//
//        assertEquals("1", workoutResponse.getId());
//        assertEquals("TestWorkout", workoutResponse.getName());
//        assertEquals("TestUser129", workoutResponse.getUserCode());
//        assertEquals("TestWorkout129", workoutResponse.getWorkoutCode());
//        assertEquals(now, workoutResponse.getDate());
//        assertEquals("60", workoutResponse.getDuration());
//        assertEquals("3", workoutResponse.getSets());
//        assertEquals("10", workoutResponse.getReps());
//        assertEquals("60", workoutResponse.getPauseBetweenReps());
//        assertEquals("Strength", workoutResponse.getType());
//        assertEquals("50.0", workoutResponse.getWeight());
//        assertEquals("0.0", workoutResponse.getDistance());
//        assertEquals("0.0", workoutResponse.getSpeed());
//        assertEquals("Test description", workoutResponse.getDescription());
//    }

}
