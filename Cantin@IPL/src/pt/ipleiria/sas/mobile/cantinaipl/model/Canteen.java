package pt.ipleiria.sas.mobile.cantinaipl.model;

public class Canteen {

	// [REGION] Fields

	private int canteen_id;
	private String name;
	private String address;
	private String lunchHorary;
	private String dinnerHorary;
	private String campus;
	private int photo;
	private double latitude;
	private double longitude;

	// [ENDREGION] Fields

	// [REGION] Constructors

	public Canteen() {

	}

	public Canteen(int canteen_id, String name, String address,
			String lunchHorary, String dinnerHorary, String campus, int photo,
			double latitude, double longitude) {
		super();
		this.canteen_id = canteen_id;
		this.name = name;
		this.address = address;
		this.lunchHorary = lunchHorary;
		this.dinnerHorary = dinnerHorary;
		this.campus = campus;
		this.photo = photo;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	// [ENDREGION] Constructors

	// [REGION] Properties

	public int getCanteen_id() {
		return canteen_id;
	}

	public void setCanteen_id(int canteen_id) {
		this.canteen_id = canteen_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLunchHorary() {
		return lunchHorary;
	}

	public void setLunchHorary(String lunchHorary) {
		this.lunchHorary = lunchHorary;
	}

	public String getDinnerHorary() {
		return dinnerHorary;
	}

	public void setDinnerHorary(String dinnerHorary) {
		this.dinnerHorary = dinnerHorary;
	}

	public String getCampus() {
		return campus;
	}

	public void setCampus(String campus) {
		this.campus = campus;
	}

	public int getPhoto() {
		return photo;
	}

	public void setPhoto(int photo) {
		this.photo = photo;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	// [ENDREGION] Properties

}
