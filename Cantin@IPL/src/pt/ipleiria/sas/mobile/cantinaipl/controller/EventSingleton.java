package pt.ipleiria.sas.mobile.cantinaipl.controller;

import pt.ipleiria.sas.mobile.cantinaipl.model.CalendarEvent;

public class EventSingleton {

	private static volatile EventSingleton instance;
	private static final Object LOCK = new Object();

	public CalendarEvent calendarEvent;

	private EventSingleton() {
	}

	public static EventSingleton getInstance() {

		if (instance == null)
			synchronized (LOCK) {
				if (instance == null)
					instance = new EventSingleton();
			}

		return instance;
	}

	public CalendarEvent getEvent() {
		return calendarEvent;
	}

}
