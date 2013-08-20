package pt.ipleiria.sas.mobile.cantinaipl;

import com.actionbarsherlock.app.SherlockPreferenceActivity;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;

public class SettingsActivity extends SherlockPreferenceActivity {

	private static final String[] RATING_KEY = { "Não receber notificações",
			"Superior a 4  estrelas", "Superior a 4.5 estrelas",
			"Apenas de 5  estrelas" };
	private static final String[] RATING_VALUE = { "0", "4", "4.5", "5" };

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setPreferenceScreen(createPreferenceHierarchy());
	}

	private PreferenceScreen createPreferenceHierarchy() {

		// Root
		@SuppressWarnings("deprecation")
		PreferenceScreen root = getPreferenceManager().createPreferenceScreen(
				this);

		// Inline notificações de eventos
		PreferenceCategory inlinePrefEvents = new PreferenceCategory(this);
		inlinePrefEvents.setTitle(R.string.inline_notification_events);
		root.addPreference(inlinePrefEvents);

		// Checkbox notifications preference
		CheckBoxPreference checkboxNotification = new CheckBoxPreference(this);
		checkboxNotification.setChecked(true);
		checkboxNotification.setKey("checkbox_notifications_preference");
		checkboxNotification
				.setTitle(R.string.title_checkbox_notifications_preference);
		checkboxNotification
				.setSummary(R.string.summary_checkbox_notifications_preference);
		inlinePrefEvents.addPreference(checkboxNotification);

		// Checkbox reserves preference
		CheckBoxPreference checkboxReserves = new CheckBoxPreference(this);
		checkboxReserves.setChecked(false);
		checkboxReserves.setKey("checkbox_reserves_preference");
		checkboxReserves.setTitle(R.string.title_checkbox_reserves_preference);
		checkboxReserves
				.setSummary(R.string.summary_checkbox_reserves_preference);
		inlinePrefEvents.addPreference(checkboxReserves);

		// Checkbox reserves preference
		CheckBoxPreference checkboxEmail = new CheckBoxPreference(this);
		checkboxEmail.setChecked(true);
		checkboxEmail.setKey("checkbox_email_preference");
		checkboxEmail.setTitle(R.string.title_checkbox_email_preference);
		checkboxEmail.setSummary(R.string.summary_checkbox_email_preference);
		inlinePrefEvents.addPreference(checkboxEmail);

		// Rating dish configuration
		ListPreference listPref = new ListPreference(this);
		listPref.setEntries(RATING_KEY);
		listPref.setEntryValues(RATING_VALUE);
		listPref.setDefaultValue("0");
		listPref.setDialogTitle("Notificar pratos");
		listPref.setKey("list_preference");
		listPref.setTitle("Pratos");
		listPref.setSummary("Receber notificações da classificação dos pratos");
		// dialogBasedPrefCat.addPreference(listPref);
		inlinePrefEvents.addPreference(listPref);

		return root;

	}

}
