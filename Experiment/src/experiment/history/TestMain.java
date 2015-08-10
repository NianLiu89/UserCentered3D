package experiment.history;

import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.leapmotion.leap.Controller;

import experiment.utility.CoordinateMapper;

public class TestMain extends Application {

    private Controller c;
    private TestListener l;
    
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Point3D a = new Point3D(-1,-1,-1);
        Point3D b = new Point3D(3,4,5);
        
        double angel = a.angle(b);
        
        double cos = a.dotProduct(b)/a.magnitude()/b.magnitude();
        
        System.out.println(cos);
        System.out.println(angel);
        System.out.println(Math.acos(1 + cos)/Math.PI * 180);
       // launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO Auto-generated method stub
        
        //initLeap();
        final CoordinateMapper m = new CoordinateMapper(800, 600, 500);
        Point3D a = new Point3D(0, 0, 0);
        Point3D b = new Point3D(400, 300, 0);
        Point3D c = new Point3D(800, 600, 0);
        
        Point3D sub = a.subtract(b);
        double l = Math.sqrt(sub.dotProduct(sub));
        
        double l1 = a.distance(b);
        System.out.println(m.appToLeap(a));
        System.out.println(m.appToLeap(b));
        System.out.println(m.appToLeap(c));
        
        
        Group root = new Group();
        Scene s = new Scene(root);
        primaryStage.setScene(s);
        primaryStage.setWidth(50);
        primaryStage.setHeight(50);
        primaryStage.show();
    }
    
    private void initLeap() {
        c = new Controller();
        l = new TestListener();
        c.addListener(l);
    }

}
