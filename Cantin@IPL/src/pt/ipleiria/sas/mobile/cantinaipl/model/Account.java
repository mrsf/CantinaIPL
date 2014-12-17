package pt.ipleiria.sas.mobile.cantinaipl.model;

import java.io.Serializable;

public class Account implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private double balance;
	private String userLogin;
	
	public Account(int id, double balance, String userLogin) {
		this.id = id;
		this.balance = balance;
		this.userLogin = userLogin;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getUserLogin() {
		return userLogin;
	}

	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}
	
}
