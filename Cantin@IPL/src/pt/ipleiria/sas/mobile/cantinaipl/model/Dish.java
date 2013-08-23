package pt.ipleiria.sas.mobile.cantinaipl.model;

import java.util.Observable;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * <b>Model class to store a dish.</b>
 * 
 * <p>
 * This class, allows dish data storing. Accessing this class, your data can be
 * used for other classes. It is a parcelable class. A dish is associate to a
 * meal.
 * </p>
 * 
 * @author Márcio Francisco and Mário Correia
 * @version 2013.0612
 * @since 1.0
 * 
 */
public class Dish extends Observable implements Parcelable {

	// [REGION] Fields

	private int id;
	private String photo;
	private String description;
	private String name;
	private double price;
	private String type;
	private double rating;

	// [ENDREGION] Fields

	// [REGION] Constructors

	public Dish() {
		super();
	}

	public Dish(Parcel in) {
		super();
		this.readFromParcel(in);
	}

	public Dish(int id, String photo, String description, String name,
			double price, String type, double rating) {
		super();
		this.id = id;
		this.photo = photo;
		this.description = description;
		this.name = name;
		this.price = price;
		this.type = type;
		this.rating = rating;
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

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
		super.setChanged();
		super.notifyObservers();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
		super.setChanged();
		super.notifyObservers();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
		super.setChanged();
		super.notifyObservers();
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
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
		dest.writeString(this.photo);
		dest.writeString(this.description);
		dest.writeString(this.name);
		dest.writeDouble(this.price);
		dest.writeString(this.type);
		dest.writeDouble(this.rating);
	}

	private void readFromParcel(Parcel in) {
		this.id = in.readInt();
		this.photo = in.readString();
		this.description = in.readString();
		this.name = in.readString();
		this.price = in.readDouble();
		this.type = in.readString();
		this.rating = in.readDouble();
	}

	public static final Parcelable.Creator<Dish> CREATOR = new Parcelable.Creator<Dish>() {

		@Override
		public Dish createFromParcel(Parcel source) {
			return new Dish(source);
		}

		@Override
		public Dish[] newArray(int size) {
			return new Dish[size];
		}
	};

	// [ENDREGION] Parcelable_Code

}
