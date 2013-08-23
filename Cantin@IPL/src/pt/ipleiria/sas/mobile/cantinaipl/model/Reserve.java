package pt.ipleiria.sas.mobile.cantinaipl.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * <b>Model class to store a reserve.</b>
 * 
 * <p>
 * This class, allows reserve data storing. Accessing this class, your data can
 * be used for other classes. It is a parcelable class.
 * </p>
 * 
 * @author Márcio Francisco and Mário Correia
 * @version 2013.0620
 * @since 1.0
 * 
 */
public class Reserve extends Observable implements Parcelable {

	// [REGION] Fields

	private int id;
	private String purchaseDate;
	private String useDate;
	private double price;
	private boolean isValid;
	private String userName;
	private int mealId;
	private boolean isAccounted;
	private List<Dish> dishes;
	private int canteenId;
	private String type;

	// [ENDREGION] Fields

	// [REGION] Constructors

	public Reserve() {
		super();
	}

	public Reserve(Parcel in) {
		super();
		this.readFromParcel(in);
	}

	public Reserve(int id, String purchaseDate, String useDate, double price,
			boolean isValid, String userName, int mealId, boolean isAccounted,
			List<Dish> dishes, int canteenId, String type) {
		super();
		this.id = id;
		this.purchaseDate = purchaseDate;
		this.useDate = useDate;
		this.price = price;
		this.isValid = isValid;
		this.userName = userName;
		this.mealId = mealId;
		this.isAccounted = isAccounted;
		this.dishes = dishes;
		this.canteenId = canteenId;
		this.type = type;
		super.setChanged();
		super.notifyObservers();
	}

	// [ENDREGION] Constructors

	// [REGION] GettersAndSetters

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
		super.setChanged();
		super.notifyObservers();
	}

	public String getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(String purchaseDate) {
		this.purchaseDate = purchaseDate;
		super.setChanged();
		super.notifyObservers();
	}

	public String getUseDate() {
		return useDate;
	}

	public void setUseDate(String useDate) {
		this.useDate = useDate;
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

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
		super.setChanged();
		super.notifyObservers();
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
		super.setChanged();
		super.notifyObservers();
	}

	public int getMealId() {
		return mealId;
	}

	public void setMealId(int mealId) {
		this.mealId = mealId;
		super.setChanged();
		super.notifyObservers();
	}

	public boolean isAccounted() {
		return isAccounted;
	}

	public void setAccounted(boolean isAccounted) {
		this.isAccounted = isAccounted;
		super.setChanged();
		super.notifyObservers();
	}

	public List<Dish> getDishes() {
		return dishes;
	}

	public void setDishes(List<Dish> dishes) {
		this.dishes = dishes;
		super.setChanged();
		super.notifyObservers();
	}

	public int getCanteenId() {
		return canteenId;
	}

	public void setCanteenId(int canteenId) {
		this.canteenId = canteenId;
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

	// [ENDREGION] GettersAndSetters

	// [REGION] Parcelable_Code

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.id);
		dest.writeString(this.purchaseDate);
		dest.writeString(this.useDate);
		dest.writeDouble(this.price);
		dest.writeInt(this.isValid ? 1 : 0);
		dest.writeString(this.userName);
		dest.writeInt(this.mealId);
		dest.writeInt(this.isAccounted ? 1 : 0);
		dest.writeList(this.dishes);
		dest.writeInt(this.canteenId);
		dest.writeString(this.type);
	}

	private void readFromParcel(Parcel in) {
		this.id = in.readInt();
		this.purchaseDate = in.readString();
		this.useDate = in.readString();
		this.price = in.readDouble();
		this.isValid = (in.readInt() == 1 ? true : false);
		this.userName = in.readString();
		this.mealId = in.readInt();
		this.isAccounted = (in.readInt() == 1 ? true : false);
		this.dishes = new LinkedList<Dish>();
		in.readList(this.dishes, Dish.class.getClassLoader());
		this.canteenId = in.readInt();
		this.type = in.readString();
	}

	public static final Parcelable.Creator<Reserve> CREATOR = new Parcelable.Creator<Reserve>() {

		@Override
		public Reserve createFromParcel(Parcel source) {
			return new Reserve(source);
		}

		@Override
		public Reserve[] newArray(int size) {
			return new Reserve[size];
		}
	};

	// [ENDREGION] Parcelable_Code

}
