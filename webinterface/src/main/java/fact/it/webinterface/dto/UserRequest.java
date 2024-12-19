package fact.it.webinterface.dto;

public class UserRequest {
    private String id;
    private String userCode;
    private String name;
    private int age;
    private String height;
    private String weight;
    private String fitnessGoals;
    private Boolean male;
    private String email;

    public UserRequest() {
    }
    public UserRequest(String id, String userCode, String name, int age, Boolean male, String height, String weight, String fitnessGoals, String email) {
        this.id = id;
        this.userCode = userCode;
        this.name = name;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.fitnessGoals = fitnessGoals;
        this.male = male;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getMale() {
        return male;
    }

    public void setMale(Boolean male) {
        this.male = male;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getFitnessGoals() {
        return fitnessGoals;
    }

    public void setFitnessGoals(String fitnessGoals) {
        this.fitnessGoals = fitnessGoals;
    }
}
