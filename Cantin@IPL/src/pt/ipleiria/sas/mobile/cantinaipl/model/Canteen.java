package pt.ipleiria.sas.mobile.cantinaipl.model;

import java.util.Observable;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * <b>Model class to store a canteen.</b>
 * 
 * <p>
 * This class, allows canteen data storing. Accessing this class, your data can
 * be used for other classes. It is a parcelable class.
 * </p>
 * 
 * @author Márcio Francisco and Mário Correia
 * @version 2013.0606
 * @since 1.0
 * 
 */
public class Canteen extends Observable implements Parcelable {

	// [REGION] Fields

	private int id;
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
		super();
	}

	public Canteen(Parcel in) {
		super();
		this.readFromParcel(in);
	}

	public Canteen(int id, String name, String address, String amPeriod,
			String pmPeriod, String campus, String photo, double latitude,
			double longitude, boolean active) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.amPeriod = amPeriod;
		this.pmPeriod = pmPeriod;
		this.campus = campus;
		this.photo = photo;
		this.latitude = latitude;
		this.longitude = longitude;
		this.active = active;
		super.setChanged();
		super.notifyObservers();
	}

	// [ENDREGION] Constructors

	// [REGION] GetAndSet_Methods

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
		super.setChanged();
		super.notifyObservers();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		super.setChanged();
		super.notifyObservers();
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
		super.setChanged();
		super.notifyObservers();
	}

	public String getAmPeriod() {
		return amPeriod;
	}

	public void setAmPeriod(String amPeriod) {
		this.amPeriod = amPeriod;
		super.setChanged();
		super.notifyObservers();
	}

	public String getPmPeriod() {
		return pmPeriod;
	}

	public void setPmPeriod(String pmPeriod) {
		this.pmPeriod = pmPeriod;
		super.setChanged();
		super.notifyObservers();
	}

	public String getCampus() {
		return campus;
	}

	public void setCampus(String campus) {
		this.campus = campus;
		super.setChanged();
		super.notifyObservers();
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
		super.setChanged();
		super.notifyObservers();
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
		super.setChanged();
		super.notifyObservers();
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
		super.setChanged();
		super.notifyObservers();
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
		super.setChanged();
		super.notifyObservers();
	}

	// [ENDREGION] GetAndSet_Methods

	// [REGION] Parcelable_Code

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.id);
		dest.writeString(this.name);
		dest.writeString(this.address);
		dest.writeString(this.amPeriod);
		dest.writeString(this.pmPeriod);
		dest.writeString(this.campus);
		dest.writeString(this.photo);
		dest.writeDouble(this.latitude);
		dest.writeDouble(this.longitude);
		dest.writeInt(this.active ? 1 : 0);
	}

	private void readFromParcel(Parcel in) {
		this.id = in.readInt();
		this.name = in.readString();
		this.address = in.readString();
		this.amPeriod = in.readString();
		this.pmPeriod = in.readString();
		this.campus = in.readString();
		this.photo = in.readString();
		this.latitude = in.readDouble();
		this.longitude = in.readDouble();
		this.active = (in.readInt() == 1 ? true : false);
	}

	public static final Parcelable.Creator<Canteen> CREATOR = new Parcelable.Creator<Canteen>() {

		@Override
		public Canteen createFromParcel(Parcel source) {
			return new Canteen(source);
		}

		@Override
		public Canteen[] newArray(int size) {
			return new Canteen[size];
		}
	};

	// [ENDREGION] Parcelable_Code

}
