package pt.ipleiria.sas.mobile.cantinaipl.model;

import java.util.Observable;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * <b>Model class to store a reference.</b>
 * 
 * <p>
 * This class, allows reference data storing. Accessing this class, your data
 * can be used for other classes. It is a parcelable class.
 * </p>
 * 
 * @author Márcio Francisco and Mário Correia
 * @version 2013.0630
 * @since 1.0
 * 
 */
public class Reference extends Observable implements Parcelable {

	// [REGION] Constants

	public static final String RECEIVED_STATUS = "recebida";
	public static final String REJECTED_STATUS = "rejeitada";
	public static final String ACCEPTED_STATUS = "aceite";

	// [ENDREGION] Constants

	// [REGION] Fields

	private int id;
	private String entity;
	private String reference;
	private double amount;
	private int accountId;
	private String emitionDate;
	private String expirationDate;
	private String status;
	private boolean paid;

	// [ENDREGION] Fields

	// [REGION] Constructors

	public Reference() {
		super();
		this.paid = false;
	}

	public Reference(Parcel in) {
		super();
		this.readFromParcel(in);
	}

	public Reference(int id, String entity, String reference, double amount,
			int accountId, String emitionDate, String expirationDate,
			String status, boolean paid) {
		super();
		this.id = id;
		this.entity = entity;
		this.reference = reference;
		this.amount = amount;
		this.accountId = accountId;
		this.emitionDate = emitionDate;
		this.expirationDate = expirationDate;
		this.status = status;
		this.paid = paid;
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

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
		super.setChanged();
		super.notifyObservers();
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
		super.setChanged();
		super.notifyObservers();
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
		super.setChanged();
		super.notifyObservers();
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
		super.setChanged();
		super.notifyObservers();
	}

	public String getEmitionDate() {
		return emitionDate;
	}

	public void setEmitionDate(String emitionDate) {
		this.emitionDate = emitionDate;
		super.setChanged();
		super.notifyObservers();
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
		super.setChanged();
		super.notifyObservers();
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
		super.setChanged();
		super.notifyObservers();
	}

	public boolean isPaid() {
		return paid;
	}

	public void pay() {
		this.paid = true;
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
		dest.writeString(this.entity);
		dest.writeString(this.reference);
		dest.writeDouble(this.amount);
		dest.writeInt(this.accountId);
		dest.writeString(this.emitionDate);
		dest.writeString(this.expirationDate);
		dest.writeString(this.status);
		dest.writeInt(this.paid ? 1 : 0);
	}

	private void readFromParcel(Parcel in) {
		this.id = in.readInt();
		this.entity = in.readString();
		this.reference = in.readString();
		this.amount = in.readDouble();
		this.accountId = in.readInt();
		this.emitionDate = in.readString();
		this.expirationDate = in.readString();
		this.status = in.readString();
		this.paid = (in.readInt() == 1 ? true : false);
	}

	public static final Parcelable.Creator<Reference> CREATOR = new Parcelable.Creator<Reference>() {

		@Override
		public Reference createFromParcel(Parcel source) {
			return new Reference(source);
		}

		@Override
		public Reference[] newArray(int size) {
			return new Reference[size];
		}
	};

	// [ENDREGION] Parcelable_Code

}
