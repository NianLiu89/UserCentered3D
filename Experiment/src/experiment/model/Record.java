package experiment.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Record { 
    private final int dominantHand;
    private String file;
    private int step;
    private int[] modeRecords;
    private int[] timeRecords;
    
    /**
     * 
     * @param dominantHand false: Left; true: Right
     */
    public Record(int dominantHand) {
        this.dominantHand = dominantHand;
        setFile();
        step = 0;
        modeRecords = new int[9];
        timeRecords = new int[9];
    }
    
    public final int getDominantHand() {
        return dominantHand;
    }
    
    private final void setFile() {
        String dh = dominantHand == 1 ? "L" : "R";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String now = LocalDateTime.now().format(formatter);
        
        String file = dh + now + ".leap";
        
        this.file = file;
    }

    public final String getFile() {
        return file;
    }
    
    public void setRecords(int mode, int time) {
        if (step == 9) {
            System.out.println("All 9 records have been set");
            return;
        }
        modeRecords[step] = mode;
        timeRecords[step] = time;
        
        System.out.println("The " + (step+1) + " record has been set.");
        step++;
    }
    
    public int getModeRecord(int index) {
        return modeRecords[index];
    }
    
    public int getTimeRecord(int index) {
        return timeRecords[index];
    }

    public final void setAllTimes(int[] timeRecords) {
        this.timeRecords = timeRecords;
    }
    
    public int getStep() {
        return step;
    }

}
