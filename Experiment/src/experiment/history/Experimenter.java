package experiment.history;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Experimenter {

	private final IntegerProperty gender;
	private final IntegerProperty dominantHand;
	
	public Experimenter(Gender gender, DominantHand dominantHand) {
		this.gender = new SimpleIntegerProperty(gender.getGender());
		this.dominantHand = new SimpleIntegerProperty(dominantHand.getDominantHand());
	}

	public final IntegerProperty genderProperty() {
		return this.gender;
	}

	public final int getGender() {
		return this.genderProperty().get();
	}

	public final void setGender(final int gender) {
		this.genderProperty().set(gender);
	}

	public final IntegerProperty dominantHandProperty() {
		return this.dominantHand;
	}

	public final int getDominantHand() {
		return this.dominantHandProperty().get();
	}

	public final void setDominantHand(final int dominantHand) {
		this.dominantHandProperty().set(dominantHand);
	}

	@Override
	public String toString() {
	    Gender g = new Gender(getGender());
	    DominantHand dh = new DominantHand(getDominantHand());
	    return "Experimenter Info\nGender : " + g + "\nDominant Hand : " + dh;
	}

	
	
	
}
