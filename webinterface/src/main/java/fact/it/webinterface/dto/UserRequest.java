package fact.it.webinterface.dto;

public class UserRequest {
    private String name;
    private String age;
    private String height;
    private String weight;
    private String fitnessGoals;

    public UserRequest(String name, String age, String height, String weight, String fitnessGoals) {
        this.name = name;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.fitnessGoals = fitnessGoals;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
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
