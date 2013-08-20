package pt.ipleiria.sas.mobile.cantinaipl.model;

import java.io.Serializable;

/**
 * Model class to store a canteen.
 * 
 * This class, allows canteen data storing. Accessing this class,
 * your data can be used for other classes. It is a serialized class.
 * 
 * @author Márcio Francisco and Mário Correia
 * @version 2013.0606
 * @since 1.0
 * 
 */
public class Canteen implements Serializable {

	// [REGION] Constants
	
	private static final long serialVersionUID = 1L;
	
	// [ENDREGION] Constants
	
	// [REGION] Fields

	private int canteenid;
	private String name;
	private String address;
	private String amPeriod;
	private String pmPeriod;
	private String campus;
	private String photo;
	private double latitude;
	private double longitude;
	private boolean active;

	// [ENDREGION] Fields

	// [REGION] Constructors

	public Canteen() {

	}

	public Canteen(int canteenid, String name, String address, String amPeriod,
			String pmPeriod, String campus, String photo, double latitude,
			double longitude, boolean active) {
		this.canteenid = canteenid;
		this.name = name;
		this.address = address;
		this.amPeriod = amPeriod;
		this.pmPeriod = pmPeriod;
		this.campus = campus;
		this.photo = photo;
		this.latitude = latitude;
		this.longitude = longitude;
		this.active = active;
	}

	// [ENDREGION] Constructors

	// [REGION] GetAndSet_Methods

	public int getCanteenid() {
		return canteenid;
	}

	public void setCanteenid(int canteenid) {
		this.canteenid = canteenid;
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

	public String getAmPeriod() {
		return amPeriod;
	}

	public void setAmPeriod(String amPeriod) {
		this.amPeriod = amPeriod;
	}

	public String getPmPeriod() {
		return pmPeriod;
	}

	public void setPmPeriod(String pmPeriod) {
		this.pmPeriod = pmPeriod;
	}

	public String getCampus() {
		return campus;
	}

	public void setCampus(String campus) {
		this.campus = campus;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	// [ENDREGION] GetAndSet_Methods

}
