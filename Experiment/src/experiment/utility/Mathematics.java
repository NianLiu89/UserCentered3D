package experiment.utility;

import javafx.geometry.Point3D;

public class Mathematics {

    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    
    public static final String FXML_URL = "view/";
    public static final String INITIALIZATION_SCREEN = "Initialization.fxml";
    
    public static final String TRANSLATION_SCREEN1 = "Translation1.fxml";
    public static final String TRANSLATION_SCREEN2 = "Translation2.fxml";
    public static final String TRANSLATION_SCREEN3 = "Translation3.fxml";
    
    public static final String ROTATION_SCREEN1 = "Rotation1.fxml";
    public static final String ROTATION_SCREEN2 = "Rotation2.fxml";
    public static final String ROTATION_SCREEN3 = "Rotation3.fxml";
    
    public static final String SCALING_SCREEN1 = "Scaling1.fxml";
    public static final String SCALING_SCREEN2 = "Scaling2.fxml";
    public static final String SCALING_SCREEN3 = "Scaling3.fxml";
    
    public static final String THANKS_SCREEN = "Thanks.fxml";
    
    public static final int FADEOFF_DURATION = 500;
    
    public static final Point3D CalculateCenterPoint(Point3D thumb, Point3D index, Point3D middle) {
        return thumb.midpoint(middle).midpoint(index);
    }
    
    public static final Point3D CalculateNormalVector(Point3D thumb, Point3D index, Point3D middle) {
        Point3D vector1 = middle.subtract(thumb);   // one vector on the plane, from thumb to middle
        Point3D vector2 = index.subtract(thumb);    // another vector on the plane, from thumb to index
        return vector2.crossProduct(vector1);
    }
}
