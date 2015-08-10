package analyzer;

public class LostData {

    
    private double minMinDistance;
    private double maxMinDistance;
    private double sumMinDistance;
    private int count;
    
    public LostData(double minMinDistance, double maxMinDistance,
            double sumMinDistance, int count) {
        this.minMinDistance = minMinDistance;
        this.maxMinDistance = maxMinDistance;
        this.sumMinDistance = sumMinDistance;
        this.count = count;
    }

    public double getMinMinDistance() {
        return minMinDistance;
    }

    public double getMaxMinDistance() {
        return maxMinDistance;
    }

    public double getSumMinDistance() {
        return sumMinDistance;
    }

    public int getCount() {
        return count;
    }

    public void setMinMinDistance(double minMinDistance) {
        this.minMinDistance = minMinDistance;
    }

    public void setMaxMinDistance(double maxMinDistance) {
        this.maxMinDistance = maxMinDistance;
    }

    public void setSumMinDistance(double sumMinDistance) {
        this.sumMinDistance = sumMinDistance;
    }

    public void setCount(int count) {
        this.count = count;
    }
    
}
