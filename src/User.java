public class User {

    private SynchronizedHashMap<Integer, Results> resultList = new SynchronizedHashMap<>();

    private int avgSize;
    private int id;
    private double totalDistance;
    private double totalElevation;
    private double totalTime;
    private double avgDistance;
    private double avgElevation;
    private double avgTime;

    public User(int id) {
        this.id = id;
        this.totalDistance = 0.0;
        this.totalElevation = 0.0;
        this.totalTime = 0.0;
        this.avgDistance = 0.0;
        this.avgElevation = 0.0;
        this.avgTime = 0.0;
        this.avgSize = 0;
    }

    public int getId() {
        return this.id;
    }

    public void setName(int id) {
        this.id = id;
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

    public double getAvgDistance() {
        return avgDistance;
    }

    public void setAvgDistance(double avgDistance) {
        this.avgDistance = avgDistance;
    }

    public double getAvgElevation() {
        return avgElevation;
    }

    public void setAvgElevation(double avgElevation) {
        this.avgElevation = avgElevation;
    }

    public double getAvgTime() {
        return avgTime;
    }

    public void setAvgTime(double avgTime) {
        this.avgTime = avgTime;
    }

    public SynchronizedHashMap<Integer, Results> getResultList() {
        return resultList;
    }

    public void setResultList(SynchronizedHashMap<Integer, Results> resultList) {
        this.resultList = resultList;
    }
    
    public void updateStatistics(double distance, double time, double elevation) 
    {
        this.totalDistance = this.totalDistance + distance;
        this.totalTime = this.totalTime + time;
        this.totalElevation = this.totalElevation + elevation;

        // New average size = (Old average size * Total number of values before new value + New value) / (Total number of values before new value + 1)
        this.avgDistance = (this.avgDistance * this.avgSize + distance) / (this.avgSize + 1);
        this.avgElevation = (this.avgElevation * this.avgSize + elevation) / (this.avgSize + 1);
        this.avgTime = (this.avgTime * this.avgSize + time) / (this.avgSize + 1);

        this.avgSize++;
    }
}
