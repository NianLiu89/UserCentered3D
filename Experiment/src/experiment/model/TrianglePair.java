package experiment.model;

public class TrianglePair {

    private final Triangle left;
    private final Triangle right;
    
    public TrianglePair(Triangle left, Triangle right) {
        this.left = left;
        this.right = right;
    }
    
    public Triangle getLeft() {
        return left;
    }
    
    public Triangle getRight() {
        return right;
    }
    
}
