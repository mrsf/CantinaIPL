package pt.ipleiria.sas.mobile.cantinaipl.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Model class to store the authentication.
 * 
 * This class, allows authentication response storing. Accessing this class,
 * your data can be used for other classes. It is a serialized class.
 * 
 * @author Márcio Francisco and Mário Correia
 * @version 2013.0606
 * @since 1.0
 * 
 */
public class Authentication implements Parcelable {

	// [REGION] Fields

	private boolean userExist;
	private boolean isFirstLogin;
	private String message;

	// [ENDREGION] Fields

	// [REGION] Constructors

	public Authentication(boolean userExist, boolean isFirstLogin,
			String message) {
		this.userExist = userExist;
		this.isFirstLogin = isFirstLogin;
		this.message = message;
	}
	
	public Authentication(Parcel in) {
		this.readFromParcel(in);
	}

	// [ENDREGION] Constructors

	// [REGION] GetAndSet_Methods

	public boolean isUserExist() {
		return userExist;
	}

	public void setUserExist(boolean userExist) {
		this.userExist = userExist;
	}

	public boolean isFirstLogin() {
		return isFirstLogin;
	}

	public void setFirstLogin(boolean isFirstLogin) {
		this.isFirstLogin = isFirstLogin;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	// [ENDREGION] GetAndSet_Methods

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(String.valueOf(this.userExist));
		dest.writeString(String.valueOf(this.isFirstLogin()));
		dest.writeString(this.message);
	}

	private void readFromParcel(Parcel in) {
		this.userExist = Boolean.parseBoolean(in.readString());
		this.isFirstLogin = Boolean.parseBoolean(in.readString());
		this.message = in.readString();
	}

	public static final Parcelable.Creator<Authentication> CREATOR = new Parcelable.Creator<Authentication>() {

		@Override
		public Authentication createFromParcel(Parcel source) {
			return new Authentication(source);
		}

		@Override
		public Authentication[] newArray(int size) {
			return new Authentication[size];
		}
	};

}
