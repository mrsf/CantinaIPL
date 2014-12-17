package pt.ipleiria.sas.mobile.cantinaipl.controller;

import pt.ipleiria.sas.mobile.cantinaipl.model.Account;

public class AccountSingleton {

	private static volatile AccountSingleton instance;
	private static final Object LOCK = new Object();

	public Account account;

	private AccountSingleton() {
	}

	public static AccountSingleton getInstance() {

		if (instance == null)
			synchronized (LOCK) {
				if (instance == null)
					instance = new AccountSingleton();
			}

		return instance;
	}

	public Account getAccount() {
		return account;
	}

}
