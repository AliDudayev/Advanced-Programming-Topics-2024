package fact.it.userservice;

import fact.it.userservice.dto.RecordResponse;
import fact.it.userservice.dto.UserRequest;
import fact.it.userservice.dto.UserResponse;
import fact.it.userservice.model.User;
import fact.it.userservice.repository.UserRepository;
import fact.it.userservice.service.UserService;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
class UserServiceApplicationTests {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Test
    void contextLoads() {
    }

    @Test
    @Order(1)
    public void createUser_WithValidRequest_UserIsSaved() {
        // I want to make a unittest here that creates a user and checks if it is saved in the database
        UserRequest userRequest = UserRequest.builder()
                .userCode("TestUser126")
                .name("Test")
                .age(20)
                .height(1.80)
                .weight(80.0)
                .email("test")
                .phoneNr("123")
                .fitnessGoals("test")
                .male(true)
                .build();
        userService.createUser(userRequest);

        // To test that there are no errors when creating a user that already exists
        userService.createUser(userRequest);

//        User user = userRepository.findByUserCode("TestUser126");
        UserResponse user = userService.getUserByCode("TestUser126");

        assertEquals("TestUser126", user.getUserCode());
        assertEquals("Test", user.getName());
        assertEquals(1.80, user.getHeight());
        assertEquals(80.0, user.getWeight());
        assertEquals("test", user.getEmail());
        assertTrue(user.isMale());
        assertEquals("test", user.getEmail());
        assertEquals("test", user.getFitnessGoals());
    }

    // Write me a test that checks if the record of a user is correctly retrieved
    @Test
    @Order(2)
    public void getRecordOfUser_WithValidUserCode_ReturnsUserRecord() {
        UserRequest userRequest = UserRequest.builder()
                .userCode("TestUser127")
                .name("Test")
                .age(20)
                .height(1.80)
                .weight(80.0)
                .email("test")
                .phoneNr("123")
                .male(true)
                .fitnessGoals("test")
                .build();
        userService.createUser(userRequest);

        assertEquals("TestUser127", userService.getRecordOfUser("TestUser127").getUserCode());
        assertEquals(1000.0, userService.getRecordOfUser("TestUser127").getFastestTime());
        assertEquals(0.0, userService.getRecordOfUser("TestUser127").getLongestDistance());
        assertEquals(0.0, userService.getRecordOfUser("TestUser127").getMaxWeightLifted());
        assertEquals(0.0, userService.getRecordOfUser("TestUser127").getLongestWorkoutDuration());
        assertEquals(0.0, userService.getRecordOfUser("TestUser127").getMostCaloriesBurned());
    }

    // Write me a test that checks getuserbycode
    @Test
    @Order(3)
    public void getUserByCode_WithNonExistentCode_ReturnsNull () {
        assertNull(userService.getUserByCode("DezeUserCodeBestaatNiet"));
    }

    // Write me a test that checks getuserbycode
    @Test
    @Order(4)
    public void getUserByCode_WithExistingCode_ReturnsUserDetails() {
        assertEquals("TestUser127", userService.getUserByCode("TestUser127").getUserCode());
        assertEquals("Test", userService.getUserByCode("TestUser127").getName());
        assertEquals(1.80, userService.getUserByCode("TestUser127").getHeight());
        assertEquals(80.0, userService.getUserByCode("TestUser127").getWeight());
        assertTrue(userService.getUserByCode("TestUser127").isMale());
        assertEquals("test", userService.getUserByCode("TestUser127").getEmail());
        assertEquals("test", userService.getUserByCode("TestUser127").getFitnessGoals());
    }

    // Write me a test that checks getAllUsers
    @Test
    @Order(5)
    public void getAllUsers_WhenUsersExist_ReturnsAllUsers() {
        assertEquals("TestUser127", userService.getAllUsers().get(userService.getAllUsers().size() - 1).getUserCode());
        assertEquals("TestUser126", userService.getAllUsers().get(userService.getAllUsers().size() - 2).getUserCode());
    }

    // Write me a test that checks updateUser
    @Test
    @Order(6)
    public void updateUser_WithExistingUser_UpdatesUserSuccessfully() {
        UserRequest userRequest = UserRequest.builder()
                .userCode("TestUser128")
                .name("Test")
                .age(20)
                .height(1.80)
                .weight(80.0)
                .email("test")
                .phoneNr("123")
                .male(true)
                .fitnessGoals("test")
                .build();
        userService.createUser(userRequest);

        userRequest = UserRequest.builder()
                .userCode("TestUser999")
                .name("UpdatedTest")
                .age(30)
                .height(2.00)
                .weight(100.0)
                .email("UpdatedTest")
                .phoneNr("999")
                .male(false)
                .fitnessGoals("updatedTest")
                .build();

        userService.updateUser("TestUser128", userRequest);
        userService.updateUser("FakeUser", userRequest);

        assertEquals("UpdatedTest", userService.getUserByCode("TestUser128").getName());
        assertEquals(2.00, userService.getUserByCode("TestUser128").getHeight());
        assertEquals(100.0, userService.getUserByCode("TestUser128").getWeight());
        assertEquals("UpdatedTest", userService.getUserByCode("TestUser128").getEmail());
        assertFalse(userService.getUserByCode("TestUser128").isMale());
        assertEquals("updatedTest", userService.getUserByCode("TestUser128").getFitnessGoals());
    }

    // Write me a test that checks deleteUser
    @Test
    @Order(7)
    public void deleteUser_WithExistingUser_RemovesUserSuccessfully() {
        userService.deleteUser("TestUser128");
        assertNull(userService.deleteUser("FakeUser"));
        assertNull(userService.getUserByCode("TestUser128"));
        assertNull(userService.getRecordOfUser("TestUser128"));
    }

    // Write me a test that checks getAllRecords
    @Test
    @Order(8)
    public void getAllRecords_WhenRecordsExist_ReturnsAllRecords() {
        // So I want to use GetAllRecords to return a List<RecordResponse>. Then I want to check if the first and second record in the list has the correct usercode.
        assertEquals("TestUser127", userService.getAllRecords().get(userService.getAllRecords().size() - 1).getUserCode());
        assertEquals("TestUser126", userService.getAllRecords().get(userService.getAllRecords().size() - 2).getUserCode());
    }

    // Write me a test that checks updateRecord
    @Test
    @Order(9)
    public void updateRecord_WithValidData_UpdatesRecordSuccessfully() {
        // I want to update the record of a user and check if the record is updated

        RecordResponse recordResponse = RecordResponse.builder()
                .userCode("TestUser126")
                .fastestTime(100.0)
                .longestDistance(100.0)
                .maxWeightLifted(100.0)
                .longestWorkoutDuration(100.0)
                .mostCaloriesBurned(100.0)
                .build();

        userService.updateRecord("TestUser126", recordResponse);

        assertEquals(100.0, userService.getRecordOfUser("TestUser126").getFastestTime());
        assertEquals(100.0, userService.getRecordOfUser("TestUser126").getLongestDistance());
        assertEquals(100.0, userService.getRecordOfUser("TestUser126").getMaxWeightLifted());
        assertEquals(100.0, userService.getRecordOfUser("TestUser126").getLongestWorkoutDuration());
        assertEquals(100.0, userService.getRecordOfUser("TestUser126").getMostCaloriesBurned());
    }

    @Test
    @Order(10)
    public void cleanupTestUsers_RemovesTestUsers() {
        userService.deleteUser("TestUser126");
        userService.deleteUser("TestUser127");
    }
}
