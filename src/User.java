public class User {

    private String name;
    private double totalDistance;
    private double totalElevation;
    private double totalTime;

    public User(String name) {
        this.name = name;
        this.totalDistance = 0.0;
        this.totalElevation = 0.0;
        this.totalTime = 0.0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public double getTotalElevation() {
        return totalElevation;
    }

    public void setTotalElevation(double totalElevation) {
        this.totalElevation = totalElevation;
    }

    public double getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(double totalTime) {
        this.totalTime = totalTime;
    }
    
    public void updateStatistics(double distance, double time, double elevation) {
        this.totalDistance = this.totalDistance + distance;
        this.totalTime = this.totalTime + time;
        this.totalElevation = this.totalElevation + elevation;
    }
    
}
