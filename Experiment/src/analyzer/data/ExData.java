package analyzer.data;

import javafx.geometry.Point3D;

import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Vector;

public class ExData{

    private ExFrames[] frames;
    private RwFrames[] rawFrames;
    
    private int taskIndex;
    private Frame fOld;
    private Frame fNew;
    
    public ExData() {
        frames = new ExFrames[9];
        rawFrames = new RwFrames[9];
        
        taskIndex = 0;
        
        for (int i = 0; i < 9; i++) {
            frames[i] = new ExFrames();
            rawFrames[i] = new RwFrames();
        }
    }
    
    public int getDataLength() {
        return frames.length;
    }
    
    public ExFrames getDataByTaskID(int index) {
        return frames[index];
    }
    
    public RwFrames getRawDataByTaskID(int index) {
        return rawFrames[index];
    }
    
    public double getTimeOf(int index) {
        int size = frames[index].size();
        long time = frames[index].get(size-1).getTimestamp() - frames[index].get(0).getTimestamp();
        return (time/10000)/100.0;
    }
    
    public void addData(Frame f) {
        if (fNew != null) {
            fOld = fNew;
            fNew = f;
            
            long deltaTime = fNew.timestamp() - fOld.timestamp();  //MicroSeconds
            
            if (deltaTime > 500000) {
                System.out.println("Next Task " + taskIndex);
                taskIndex++;
            }
        } else {
            fNew = f;
        }
        
        Hand leapLeft;
        Hand leapRight;
        if (f.hands().count() == 2) {
            leapLeft = f.hands().get(0).isLeft() ? f.hands().get(0) : f.hands().get(1);
            leapRight = f.hands().get(0).isRight() ? f.hands().get(0) : f.hands().get(1);
        } else if (f.hands().count() == 1) {
            if (f.hands().get(0).isLeft()) {
                leapLeft = f.hands().get(0);
                leapRight = new Hand();
            } else {
                leapRight = f.hands().get(0);
                leapLeft = new Hand();
            }
        } else {
            leapLeft = new Hand();
            leapRight = new Hand();
        }
        
        ExHand exLeft = buildExHand(leapLeft);
        ExHand exRight = buildExHand(leapRight);
        
        ExFrame exFrame = new ExFrame(exLeft, exRight, f.timestamp());
        frames[taskIndex].add(exFrame);
        rawFrames[taskIndex].add(f);
    }
    
    private ExHand buildExHand(Hand hand) {
        if (hand.isValid()) {
            int thumbID = hand.id() * 10;
            Finger thumb = hand.finger(thumbID);
            Point3D thumbPoint = new Point3D(thumb.tipPosition().getX(), thumb.tipPosition().getY(), thumb.tipPosition().getZ()); 
            Point3D thumbVelocity = buildVelocity(thumb.tipVelocity());
            
            int indexID = hand.id() * 10 + 1;
            Finger index = hand.finger(indexID);
            Point3D indexPoint = new Point3D(index.tipPosition().getX(), index.tipPosition().getY(), index.tipPosition().getZ());
            Point3D indexVelocity = buildVelocity(index.tipVelocity());
            
            int middleID = hand.id() * 10 + 2;
            Finger middle = hand.finger(middleID);
            Point3D middlePoint = new Point3D(middle.tipPosition().getX(), middle.tipPosition().getY(), middle.tipPosition().getZ());
            Point3D middleVelocity = buildVelocity(middle.tipVelocity());
            
            return new ExHand(thumbPoint, indexPoint, middlePoint, thumbVelocity, indexVelocity, middleVelocity);
        }
        
        return new ExHand(new Point3D(0,0,0), new Point3D(0,0,0), new Point3D(0,0,0), new Point3D(0,0,0), new Point3D(0,0,0), new Point3D(0,0,0));
    }
    
    private Point3D buildVelocity(Vector vector) {
        return new Point3D(vector.getX(), vector.getY(), vector.getZ());
    }
    
}
