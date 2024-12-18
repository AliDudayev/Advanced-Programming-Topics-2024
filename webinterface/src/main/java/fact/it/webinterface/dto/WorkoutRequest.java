package fact.it.webinterface.dto;

public class WorkoutRequest {
    private String id;
    private String userCode;
    private String workoutCode;
    private String name;
    private String date;
    private String duration;
    private String sets;
    private String reps;
    private String type;
    private String weight;
    private String distance;
    private String speed;
    private String description;
    private String pauseBetweenReps;

    public WorkoutRequest() {
    }

    public WorkoutRequest(String id, String userCode, String workoutCode, String name, String date, String duration, String sets, String reps, String type, String weight, String distance, String speed, String description, String pauseBetweenReps) {
        this.workoutCode = workoutCode;
        this.name = name;
        this.date = date;
        this.duration = duration;
        this.sets = sets;
        this.reps = reps;
        this.type = type;
        this.weight = weight;
        this.distance = distance;
        this.speed = speed;
        this.description = description;
        this.userCode = userCode;
        this.pauseBetweenReps = pauseBetweenReps;
        this.id = id;
    }


    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPauseBetweenReps() {
        return pauseBetweenReps;
    }

    public void setPauseBetweenReps(String pauseBetweenReps) {
        this.pauseBetweenReps = pauseBetweenReps;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getWorkoutCode() {
        return workoutCode;
    }

    public void setWorkoutCode(String workoutCode) {
        this.workoutCode = workoutCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getSets() {
        return sets;
    }

    public void setSets(String sets) {
        this.sets = sets;
    }

    public String getReps() {
        return reps;
    }

    public void setReps(String reps) {
        this.reps = reps;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
