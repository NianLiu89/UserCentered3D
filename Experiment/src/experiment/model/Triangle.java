package experiment.model;

import javafx.geometry.Point3D;

public class Triangle {

    private final Point3D thumb;
    private final Point3D index;
    private final Point3D middle;
    
    public Triangle(Point3D thumb, Point3D index, Point3D middle) {
        this.thumb = thumb;
        this.index = index;
        this.middle = middle;
    }

    public Point3D getThumb() {
        return thumb;
    }

    public Point3D getIndex() {
        return index;
    }

    public Point3D getMiddle() {
        return middle;
    }
    
}
