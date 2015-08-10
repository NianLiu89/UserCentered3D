package experiment.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point3D;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture.Type;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Listener;

public class LeapListener extends Listener {
    
    private final DataManager dataManager;
    
    private int counter = 0;
    private boolean isDetecting;
    private boolean isRecordEnabled;
    private ObjectProperty<TrianglePair> handPair;
    
    private ObjectProperty<Triangle> leftHand;
    private ObjectProperty<Triangle> rightHand;
    
    public LeapListener(DataManager dataManager) {
        this.dataManager = dataManager;
        isDetecting = false;
        isRecordEnabled = false;
        leftHand= new SimpleObjectProperty<Triangle>();
        rightHand= new SimpleObjectProperty<Triangle>();
        handPair = new SimpleObjectProperty<TrianglePair>();
    }
    
    public ObjectProperty<TrianglePair> handPairProperty() {
        return handPair;
    }
    
    public ObjectProperty<Triangle> leftHandProperty() {
        return leftHand;
    }
    
    public ObjectProperty<Triangle> rightHandProperty() {
        return rightHand;
    }
    
    @Override
    public void onConnect(Controller controller) {
        controller.enableGesture(Type.TYPE_CIRCLE);
        controller.enableGesture(Type.TYPE_KEY_TAP);
        controller.enableGesture(Type.TYPE_SCREEN_TAP);
        controller.enableGesture(Type.TYPE_SWIPE);
        
        controller.setPolicy(Controller.PolicyFlag.POLICY_OPTIMIZE_HMD);
        System.out.println(controller.isPolicySet(Controller.PolicyFlag.POLICY_OPTIMIZE_HMD));
//        controller.setPolicy(Controller.PolicyFlag.POLICY_IMAGES);
//        controller.setPolicy(Controller.PolicyFlag.POLICY_BACKGROUND_FRAMES);
//        controller.setPolicy(Controller.PolicyFlag.POLICY_OPTIMIZE_HMD);
        
        System.out.println("Leap Motion Connected!");
    }
    
    @Override
    public void onFrame(Controller controller) {
        if (isDetecting) {
            Frame frame = controller.frame();
            
            if (isRecordEnabled) {
                dataManager.writeToFile(frame);
                counter++;
            }
            
            if (!frame.hands().isEmpty()) {
                Hand left = frame.hands().get(0).isLeft() ? frame.hands().get(0) : frame.hands().get(1);
                Hand right = frame.hands().get(0).isRight() ? frame.hands().get(0) : frame.hands().get(1);
                
                Triangle expLeft = left.isLeft() ? buildExpHand(left) : null;
                Triangle expRight = right.isValid() ? buildExpHand(right) : null;
                
                leftHand.setValue(expLeft);
                rightHand.setValue(expRight);
                
                handPair.setValue(new TrianglePair(expLeft, expRight));
            } else {
                leftHand.setValue(null);
                rightHand.setValue(null);
                handPair.setValue(new TrianglePair(null, null));
            }
        }
    }
    
    public DataManager getDataManager() {
        return dataManager;
    }
    
    public void startDetect() {
        isDetecting = true;
        System.out.println("Detection started.");
    }
    
    public void stopDetect() {
        isDetecting = false;
        System.out.println("Detection stopped.");
    }
    
    public boolean isDetecting() {
        return isDetecting;
    }
    
    public void switchMode() {
        isRecordEnabled = !isRecordEnabled;
        
        if (isRecordEnabled) {
            System.out.println("[Detect/Record]");
        } else {
            System.out.println("[Detect-Only]");
        }
    }
    
    public boolean isRecordEnabled() {
        return isRecordEnabled;
    }
    
    private Triangle buildExpHand(Hand hand) {
        int thumbID = hand.id() * 10;
        Finger thumb = hand.finger(thumbID);
        Point3D thumbPoint = new Point3D(thumb.tipPosition().getX(), thumb.tipPosition().getY(), thumb.tipPosition().getZ()); 
                
        int indexID = hand.id() * 10 + 1;
        Finger index = hand.finger(indexID);
        Point3D indexPoint = new Point3D(index.tipPosition().getX(), index.tipPosition().getY(), index.tipPosition().getZ());
        
        int middleID = hand.id() * 10 + 2;
        Finger middle = hand.finger(middleID);
        Point3D middlePoint = new Point3D(middle.tipPosition().getX(), middle.tipPosition().getY(), middle.tipPosition().getZ());
        
        return new Triangle(thumbPoint, indexPoint, middlePoint);
    }
    
    public void enableRecord() {
        isRecordEnabled = true;;
    }
    
    public void disableRecord() {
        isRecordEnabled = false;
    }
}
