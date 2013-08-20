package pt.ipleiria.sas.mobile.cantinaipl.model;

public class Reference {

	private int id;
	private String entity;
	private String reference;
	private double amount;
	private int accountId;
	private String emitionDate;
	private String expirationDate;
	private String status;
	private boolean isPaid;
	
	public Reference(int id, String entity, String reference, double amount,
			int accountId, String emitionDate, String expirationDate,
			String status, boolean isPaid) {
		this.id = id;
		this.entity = entity;
		this.reference = reference;
		this.amount = amount;
		this.accountId = accountId;
		this.emitionDate = emitionDate;
		this.expirationDate = expirationDate;
		this.status = status;
		this.isPaid = isPaid;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public String getEmitionDate() {
		return emitionDate;
	}

	public void setEmitionDate(String emitionDate) {
		this.emitionDate = emitionDate;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isPaid() {
		return isPaid;
	}

	public void setPaid(boolean isPaid) {
		this.isPaid = isPaid;
	}
	
}
