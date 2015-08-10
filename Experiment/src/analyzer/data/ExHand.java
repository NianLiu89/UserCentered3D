package analyzer.data;

import javafx.geometry.Point3D;

public class ExHand {

    private Point3D thumb;
    private Point3D index;
    private Point3D middle;
    
    private Point3D thumbVelocity;
    private Point3D indexVelocity;
    private Point3D middleVelocity;
    
    public ExHand(Point3D thumb, Point3D index, Point3D middle, Point3D thumbVelocity, Point3D indexVelocity, Point3D middleVelocity) {
        this.thumb = thumb;
        this.index = index;
        this.middle = middle;
        this.thumbVelocity = thumbVelocity;
        this.indexVelocity = indexVelocity;
        this.middleVelocity = middleVelocity;
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

    public Point3D getThumbVelocity() {
        return thumbVelocity;
    }

    public Point3D getIndexVelocity() {
        return indexVelocity;
    }

    public Point3D getMiddleVelocity() {
        return middleVelocity;
    }
    
}
