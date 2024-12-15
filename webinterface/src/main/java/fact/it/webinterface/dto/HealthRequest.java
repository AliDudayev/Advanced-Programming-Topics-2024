package fact.it.webinterface.dto;

public class HealthRequest {
    private String recoveryHeartRate;
    private String bloodPressure;
    private String workoutCode;
    private String caloriesBurned;
    private String oxygenSaturation;

    public HealthRequest(String recoveryHeartRate, String bloodPressure, String workoutCode, String caloriesBurned, String oxygenSaturation) {
        this.recoveryHeartRate = recoveryHeartRate;
        this.bloodPressure = bloodPressure;
        this.workoutCode = workoutCode;
        this.caloriesBurned = caloriesBurned;
        this.oxygenSaturation = oxygenSaturation;
    }

    // Getters and Setters
    public String getRecoveryHeartRate() {
        return recoveryHeartRate;
    }

    public void setRecoveryHeartRate(String recoveryHeartRate) {
        this.recoveryHeartRate = recoveryHeartRate;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public String getWorkoutCode() {
        return workoutCode;
    }

    public void setWorkoutCode(String workoutCode) {
        this.workoutCode = workoutCode;
    }

    public String getCaloriesBurned() {
        return caloriesBurned;
    }

    public void setCaloriesBurned(String caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }

    public String getOxygenSaturation() {
        return oxygenSaturation;
    }

    public void setOxygenSaturation(String oxygenSaturation) {
        this.oxygenSaturation = oxygenSaturation;
    }
}
