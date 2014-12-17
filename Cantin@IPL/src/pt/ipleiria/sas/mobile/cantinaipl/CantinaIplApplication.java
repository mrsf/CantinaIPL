package pt.ipleiria.sas.mobile.cantinaipl;

import pt.ipleiria.sas.mobile.cantinaipl.controller.AccountSingleton;
import pt.ipleiria.sas.mobile.cantinaipl.controller.EventSingleton;
import pt.ipleiria.sas.mobile.cantinaipl.controller.UserSingleton;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

public class CantinaIplApplication extends Application {

	public static final String APP_PASSWORD = "ipl.cantina.1213@gmail.com";

	private SharedPreferences appConfigurations;

	@Override
	public void onCreate() {
		super.onCreate();
		this.appConfigurations = PreferenceManager
				.getDefaultSharedPreferences(this);
		initSingletons();
	}

	protected void initSingletons() {
		UserSingleton.getInstance();
		EventSingleton.getInstance();
		AccountSingleton.getInstance();
	}

	public boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();

		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	public boolean isNotificationsActive() {
		return this.appConfigurations.getBoolean(
				"checkbox_notifications_preference", true);
	}

	public boolean isCalendarEventsActive() {
		return this.appConfigurations.getBoolean(
				"checkbox_reserves_preference", false);
	}
	
	public String isMealAlertActive() {
		return this.appConfigurations.getString("list_preference", "0");
	}

	public boolean isEmailsActive() {
		return this.appConfigurations.getBoolean("checkbox_email_preference",
				true);
	}

}
