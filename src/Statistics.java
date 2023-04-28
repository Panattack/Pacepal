import java.io.Serializable;
public class Statistics implements Serializable {
    
    private double globalAvgTime=0.0;
    private double globalAvgDistance=0.0;
    private double globalAvgElevation=0.0;
    private int globalSize=0;
    
    public Statistics(double globalAvgTime, double globalAvgDistance, double globalAvgElevation, int globalSize) {
        this.globalAvgTime = globalAvgTime;
        this.globalAvgDistance = globalAvgDistance;
        this.globalAvgElevation = globalAvgElevation;
        this.globalSize = globalSize;
    }

    public double getGlobalAvgTime() {
        return globalAvgTime;
    }
    public void setGlobalAvgTime(double globalAvgTime) {
        this.globalAvgTime = globalAvgTime;
    }
    public double getGlobalAvgDistance() {
        return globalAvgDistance;
    }
    public void setGlobalAvgDistance(double globalAvgDistance) {
        this.globalAvgDistance = globalAvgDistance;
    }
    public double getGlobalAvgElevation() {
        return globalAvgElevation;
    }
    public void setGlobalAvgElevation(double globalAvgElevation) {
        this.globalAvgElevation = globalAvgElevation;
    }
    public int getGlobalSize() {
        return globalSize;
    }
    public void setGlobalSize(int globalSize) {
        this.globalSize = globalSize;
    }





}
