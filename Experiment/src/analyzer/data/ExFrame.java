package analyzer.data;

public class ExFrame {

    private ExHand left;
    private ExHand right;
    private long timestamp;
    
    public ExFrame(ExHand left, ExHand right, long timestamp) {
        this.left = left;
        this.right = right;
        this.timestamp = timestamp;
    }

    public ExHand getLeft() {
        return left;
    }

    public ExHand getRight() {
        return right;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
}
