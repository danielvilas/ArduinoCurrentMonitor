package es.daniel.datalogger.data;

/**
 * Created by daniel on 11/6/17.
 */
public class PacketAnalysData {
    int max=Integer.MIN_VALUE;
    int min = Integer.MAX_VALUE;
    int peaks=0;

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getPeaks() {
        return peaks;
    }

    public void setPeaks(int peaks) {
        this.peaks = peaks;
    }

    public float getAverage() {
        return average;
    }

    public void setAverage(float average) {
        this.average = average;
    }

    float average;

    @Override
    public String toString() {
        return max +", "+min+", "+ average+", "+peaks;
    }
}
