package experiment.history;

public class Gender {
	
	private static final int G_FEMAL = 0;
	private static final int G_MALE = 1;
	
	public static final Gender FEMALE = new Gender(0);
	public static final Gender MALE = new Gender(1);
	
	private final int gender;
	
	/**
	 * The gender of a experiment.
	 * @param gender Can only be 0 (Female) or 1 (Male).
	 */
	public Gender(int gender) {
		if (gender < G_FEMAL || gender > G_MALE) {
			throw new IllegalArgumentException("Illegal parameter for the constructor of Gender! Use Class Constants!");
		}
		
		this.gender = gender;
	}

	public int getGender() {
		return gender;
	}
	
	public boolean isFemale() {
		return gender == G_FEMAL;
	}
	
	public boolean isMale() {
		return gender == G_MALE;
	}
	
	@Override
	public String toString() {
	    return isFemale() ? "Female" : "Male";
	}
	
}
