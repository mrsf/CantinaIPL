package pt.ipleiria.sas.mobile.cantinaipl.model;

import java.util.Observable;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * <b>Model class to store a user.</b>
 * 
 * <p>
 * This class, allows user data storing. Accessing this class, your data can be
 * used for other classes. It is a parcelable class.
 * </p>
 * 
 * @author Márcio Francisco and Mário Correia
 * @version 2013.0606
 * @since 1.0
 * 
 */
public class User extends Observable implements Parcelable {

	// [REGION] Constants

	public static final boolean STUDENT_TYPE = false;
	public static final boolean EMPLOYEE_TYPE = true;
	public static final boolean DAYTIME_REGIME = false;
	public static final boolean POST_EMPLOYMENT_REGIME = true;

	// [ENDREGION] Constants

	// [REGION] Fields

	private String userName;
	private int bi;
	private String name;
	private String course;
	private boolean regime;
	private String photo;
	private int nif;
	private String email;
	private boolean type;
	private boolean active;
	private String school;

	// [ENDREGION] Fields

	// [REGION] Constructors

	public User() {
		super();
	}

	public User(Parcel in) {
		super();
		this.readFromParcel(in);
	}

	public User(String userName, int bi, String name, String course,
			boolean regime, String photo, int nif, String email, boolean type,
			boolean active, String school) {
		super();
		this.userName = userName;
		this.bi = bi;
		this.name = name;
		this.course = course;
		this.regime = regime;
		this.photo = photo;
		this.nif = nif;
		this.email = email;
		this.type = type;
		this.active = active;
		this.school = school;
		super.setChanged();
		super.notifyObservers();
	}

	// [ENDREGION] Constructors

	// [REGION] GetAndSet_Methods

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
		super.setChanged();
		super.notifyObservers();
	}

	public int getBi() {
		return bi;
	}

	public void setBi(int bi) {
		this.bi = bi;
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

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
		super.setChanged();
		super.notifyObservers();
	}

	public boolean getRegime() {
		return regime;
	}

	public void setRegime(boolean regime) {
		this.regime = regime;
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

	public int getNif() {
		return nif;
	}

	public void setNif(int nif) {
		this.nif = nif;
		super.setChanged();
		super.notifyObservers();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
		super.setChanged();
		super.notifyObservers();
	}

	public boolean getType() {
		return type;
	}

	public void setType(boolean type) {
		this.type = type;
		super.setChanged();
		super.notifyObservers();
	}

	public boolean isActive() {
		return active;
	}

	public void activate() {
		this.active = true;
		super.setChanged();
		super.notifyObservers();
	}

	public void desactivate() {
		this.active = false;
		super.setChanged();
		super.notifyObservers();
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
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
		dest.writeString(this.userName);
		dest.writeInt(this.bi);
		dest.writeString(this.name);
		dest.writeString(this.course);
		dest.writeInt(this.regime ? 1 : 0);
		dest.writeString(this.photo);
		dest.writeInt(this.nif);
		dest.writeString(this.email);
		dest.writeInt(this.type ? 1 : 0);
		dest.writeInt(this.active ? 1 : 0);
		dest.writeString(this.school);
	}

	private void readFromParcel(Parcel in) {
		this.userName = in.readString();
		this.bi = in.readInt();
		this.name = in.readString();
		this.course = in.readString();
		this.regime = (in.readInt() == 1 ? true : false);
		this.photo = in.readString();
		this.nif = in.readInt();
		this.email = in.readString();
		this.type = (in.readInt() == 1 ? true : false);
		this.active = (in.readInt() == 1 ? true : false);
		this.school = in.readString();
	}

	public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {

		@Override
		public User createFromParcel(Parcel source) {
			return new User(source);
		}

		@Override
		public User[] newArray(int size) {
			return new User[size];
		}
	};

	// [ENDREGION] Parcelable_Code

}
