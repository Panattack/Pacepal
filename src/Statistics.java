import java.io.Serializable;
public class Statistics implements Serializable {
    
    private double globalAvgTime;
    private double globalAvgDistance;
    private double globalAvgElevation;
    private int globalSize;
    
    public Statistics() {
        this.globalAvgTime = 0.0;
        this.globalAvgDistance = 0.0;
        this.globalAvgElevation = 0.0;
        this.globalSize = 0;
    }

    public double getGlobalAvgTime() 
    {
        return globalAvgTime;
    }

    public void setGlobalAvgTime(double globalAvgTime) 
    {
        this.globalAvgTime = globalAvgTime;
    }
    public double getGlobalAvgDistance() 
    {
        return globalAvgDistance;
    }

    public void setGlobalAvgDistance(double globalAvgDistance) 
    {
        this.globalAvgDistance = globalAvgDistance;
    }

    public double getGlobalAvgElevation() 
    {
        return globalAvgElevation;
    }

    public void setGlobalAvgElevation(double globalAvgElevation) 
    {
        this.globalAvgElevation = globalAvgElevation;
    }

    public int getGlobalSize() {
        return globalSize;
    }

    public void setGlobalSize(int globalSize) 
    {
        this.globalSize = globalSize;
    }

    public synchronized void updateValues(double AvgTime, double AvgDistance, double AvgElevation) 
    {
        this.globalAvgTime = (this.globalAvgTime * this.globalSize + AvgTime) / (this.globalSize + 1);
        this.globalAvgDistance = (this.globalAvgDistance * this.globalSize + AvgDistance) / (this.globalSize + 1);
        this.globalAvgElevation = (this.globalAvgElevation * this.globalSize + AvgElevation) / (this.globalSize + 1);

        this.globalSize++;
    }
}
