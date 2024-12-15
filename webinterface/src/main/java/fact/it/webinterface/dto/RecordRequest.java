package fact.it.webinterface.dto;

public class RecordRequest {
    private String userCode;
    private String fastestTime;
    private String longestDistance;
    private String maxWeightLifted;
    private String longestWorkoutDuration;
    private String mostCaloriesBurned;

    public RecordRequest(String userCode, String fastestTime, String longestDistance, String maxWeightLifted, String longestWorkoutDuration, String mostCaloriesBurned) {
        this.userCode = userCode;
        this.fastestTime = fastestTime;
        this.longestDistance = longestDistance;
        this.maxWeightLifted = maxWeightLifted;
        this.longestWorkoutDuration = longestWorkoutDuration;
        this.mostCaloriesBurned = mostCaloriesBurned;
    }

    // Getters and Setters
    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getFastestTime() {
        return fastestTime;
    }

    public void setFastestTime(String fastestTime) {
        this.fastestTime = fastestTime;
    }

    public String getLongestDistance() {
        return longestDistance;
    }

    public void setLongestDistance(String longestDistance) {
        this.longestDistance = longestDistance;
    }

    public String getMaxWeightLifted() {
        return maxWeightLifted;
    }

    public void setMaxWeightLifted(String maxWeightLifted) {
        this.maxWeightLifted = maxWeightLifted;
    }

    public String getLongestWorkoutDuration() {
        return longestWorkoutDuration;
    }

    public void setLongestWorkoutDuration(String longestWorkoutDuration) {
        this.longestWorkoutDuration = longestWorkoutDuration;
    }

    public String getMostCaloriesBurned() {
        return mostCaloriesBurned;
    }

    public void setMostCaloriesBurned(String mostCaloriesBurned) {
        this.mostCaloriesBurned = mostCaloriesBurned;
    }
}
