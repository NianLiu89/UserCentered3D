package experiment.history;


public class DominantHand {
	
	private static final int DH_LEFT = 0;
	private static final int DH_RIGHT = 1;
	
	public static final DominantHand LEFT = new DominantHand(0);
	public static final DominantHand RIGHT = new DominantHand(1);
	
	private final int hand;
	
	/**
	 * Left-handedness or Right-handedness.
	 * @param hand can only be 0 (left) or 1 (right).
	 */
	public DominantHand(int hand) {
		if (hand < DH_LEFT || hand > DH_RIGHT) {
			throw new IllegalArgumentException("Illegal parameter for the constructor of DominantHand! Use Class Constants!");
		}
		this.hand = hand;
	}
	
	public int getDominantHand() {
		return hand;
	}
	
	public boolean isLeft() {
		return hand == DH_LEFT;
	}
	
	public boolean isRight() {
		return hand == DH_RIGHT;
	}
	
	@Override
	public String toString() {
	    return isLeft() ? "Left" : "Right";
	}
	
}
