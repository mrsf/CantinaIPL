package pt.ipleiria.sas.mobile.cantinaipl.controller;

import pt.ipleiria.sas.mobile.cantinaipl.model.User;

public class UserSingleton {

	private static volatile UserSingleton instance;
	private static final Object LOCK = new Object();

	public User user;

	private UserSingleton() {
	}

	public static UserSingleton getInstance() {

		if (instance == null)
			synchronized (LOCK) {
				if (instance == null)
					instance = new UserSingleton();
			}

		return instance;
	}

	public User getUser() {
		return user;
	}

}
