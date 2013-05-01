package pt.ipleiria.sas.mobile.cantinaipl.model;

public class Meal {

	// [REGION] Fields

	private int mealid;
	private String name;
	private String description;
	private int photo;
	private double racking;
	private String date;

	// [ENDREGION] Fields

	// [REGION] Constructors

	public Meal(int mealid, String name, String description, int photo,
			double racking, String date) {
		this.mealid = mealid;
		this.name = name;
		this.description = description;
		this.photo = photo;
		this.racking = racking;
		this.date = date;
	}

	// [ENDREGION] Constructors

	// [REGION] Properties

	public int getMealid() {
		return mealid;
	}

	public void setMealid(int mealid) {
		this.mealid = mealid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPhoto() {
		return photo;
	}

	public void setPhoto(int photo) {
		this.photo = photo;
	}

	public double getRacking() {
		return racking;
	}

	public void setRacking(double racking) {
		this.racking = racking;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	// [ENDREGION] Properties

}