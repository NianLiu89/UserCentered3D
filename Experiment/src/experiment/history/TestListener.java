package experiment.history;

import javafx.geometry.Point3D;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Listener;

import experiment.utility.CoordinateMapper;

public class TestListener extends Listener {
    private final CoordinateMapper m = new CoordinateMapper(800, 600, 500);
    
    
    
    @Override
    public void onConnect(Controller c) {
        // TODO Auto-generated method stub
        System.out.println("Connected");
       
    }

    
    @Override
    public void onFrame(Controller c) {
        Frame f = c.frame();
        Hand h = f.hands().get(0);
        Point3D pHand = new Point3D(h.palmPosition().getX(), h.palmPosition().getY(), h.palmPosition().getZ());
        
        System.out.println("LEAP " + pHand);
        System.out.println("APP  " + m.leapToApp(pHand));
        
    }
}
