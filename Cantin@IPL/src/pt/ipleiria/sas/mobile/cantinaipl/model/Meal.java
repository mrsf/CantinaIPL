package pt.ipleiria.sas.mobile.cantinaipl.model;

import java.util.LinkedList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * <b>Model class to store a meal.</b>
 * 
 * <p>
 * This class, allows meal data storing. Accessing this class, your data can be
 * used for other classes. It is a parcelable class. A meal is associate to a
 * canteen and contains a dish list.
 * </p>
 * 
 * @author Márcio Francisco and Mário Correia
 * @version 2013.0612
 * @since 1.0
 * 
 */
public class Meal implements Parcelable {

	// [REGION] Fields

	private int id;
	private String date;
	private boolean refeicao;
	private String type;
	private List<Dish> dishes;
	private double price;

	// [ENDREGION] Fields

	// [REGION] Constructors

	public Meal(int id, String date, boolean refeicao, String type,
			List<Dish> dishes, double price) {
		this.id = id;
		this.date = date;
		this.refeicao = refeicao;
		this.type = type;
		this.dishes = dishes;
		this.price = price;
	}

	public Meal(Parcel in) {
		this.readFromParcel(in);
	}

	public Meal() {
	}

	// [ENDREGION] Constructors

	// [REGION] GetAndSet_Methods

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public boolean isRefeicao() {
		return refeicao;
	}

	public void setRefeicao(boolean refeicao) {
		this.refeicao = refeicao;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Dish> getDishes() {
		return dishes;
	}

	public void setDishes(List<Dish> dishes) {
		this.dishes = dishes;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
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
		dest.writeString(this.date);
		dest.writeInt(this.refeicao ? 1 : 0);
		dest.writeString(this.type);
		dest.writeList(this.dishes);
		dest.writeDouble(this.price);
	}

	private void readFromParcel(Parcel in) {
		this.id = in.readInt();
		this.date = in.readString();
		this.refeicao = (in.readInt() == 1 ? true : false);
		this.type = in.readString();
		this.dishes = new LinkedList<Dish>();
		in.readList(this.dishes, Dish.class.getClassLoader());
		this.price = in.readDouble();
	}

	public static final Parcelable.Creator<Meal> CREATOR = new Parcelable.Creator<Meal>() {

		@Override
		public Meal createFromParcel(Parcel source) {
			return new Meal(source);
		}

		@Override
		public Meal[] newArray(int size) {
			return new Meal[size];
		}
	};

	// [ENDREGION] Parcelable_Code

}
