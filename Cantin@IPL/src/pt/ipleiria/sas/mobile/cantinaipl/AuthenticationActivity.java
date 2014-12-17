package pt.ipleiria.sas.mobile.cantinaipl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import com.actionbarsherlock.view.MenuInflater;

import pt.ipleiria.sas.mobile.cantinaipl.controller.EventSingleton;
import pt.ipleiria.sas.mobile.cantinaipl.controller.UserSingleton;
import pt.ipleiria.sas.mobile.cantinaipl.database.CanteensRepository;
import pt.ipleiria.sas.mobile.cantinaipl.database.CantinaIplRepository;
import pt.ipleiria.sas.mobile.cantinaipl.database.DishsRepository;
import pt.ipleiria.sas.mobile.cantinaipl.database.MealsRepository;
import pt.ipleiria.sas.mobile.cantinaipl.database.ReferencesRepository;
import pt.ipleiria.sas.mobile.cantinaipl.database.ReservesRepository;
import pt.ipleiria.sas.mobile.cantinaipl.database.UsersRepository;
import pt.ipleiria.sas.mobile.cantinaipl.model.Authentication;
import pt.ipleiria.sas.mobile.cantinaipl.model.CalendarEvent;
import pt.ipleiria.sas.mobile.cantinaipl.model.SharedPreference;
import pt.ipleiria.sas.mobile.cantinaipl.model.User;
import pt.ipleiria.sas.mobile.cantinaipl.parser.SimpleCrypto;
import pt.ipleiria.sas.mobile.cantinaipl.util.SystemUiHider;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class AuthenticationActivity extends ClosableActivity {

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private AuthenticationTask mAuthTask = null;

	private SharedPreferences appPrefs;

	// Values for email and password at the time of the login attempt.
	private String mLogin;
	private String mPassword;
	private boolean mMemorized;

	// UI references.
	private EditText mLoginView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;
	private CheckBox mCheckBox;

	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	// private static final int HIDER_FLAGS =
	// SystemUiHider.FLAG_HIDE_NAVIGATION;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	// private SystemUiHider mSystemUiHider;

	@Override
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_authentication);

		this.appPrefs = getSharedPreferences("appPreferences", MODE_PRIVATE);

		EventSingleton.getInstance().calendarEvent = new CalendarEvent(
				"Reserva " + getResources().getString(R.string.app_name), "",
				"", "", "");

		// Set up the login form.
		mLoginView = (EditText) findViewById(R.id.et_login);
		// mLoginView.setText("2091112");

		mPasswordView = (EditText) findViewById(R.id.et_password);
		// mPasswordView.setText("13225534");

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		findViewById(R.id.bt_sign_in).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});

		mCheckBox = (CheckBox) findViewById(R.id.cb_memorize);

		if (savedInstanceState == null
				&& Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {

			// Set up an instance of SystemUiHider to control the system UI
			// for
			// this activity.

			/*
			 * if (hasNavigationBar()) { mSystemUiHider =
			 * SystemUiHider.getInstance(this, mLoginFormView, HIDER_FLAGS);
			 * mSystemUiHider.setup(); mSystemUiHider.hide(); }
			 */

			mCheckBox.animate().setDuration(4000).alpha(1)
					.setListener(new AnimatorListenerAdapter() {

						@Override
						public void onAnimationEnd(Animator animation) {
							super.onAnimationEnd(animation);

							if (getPreferenceData().isMemorized()) {
								mLoginView.setText(getPreferenceData()
										.getLogin());
								mPasswordView.setText(getPreferenceData()
										.getPassword());
								attemptLogin();
							} else {

								findViewById(R.id.space_object)
										.animate()
										.setDuration(20)
										.alpha(1)
										.setListener(
												new AnimatorListenerAdapter() {
													@Override
													public void onAnimationEnd(
															Animator animation) {
														super.onAnimationEnd(animation);
														findViewById(
																R.id.space_object)
																.setVisibility(
																		View.INVISIBLE);
													}
												});

								mLoginView
										.animate()
										.setDuration(40)
										.alpha(1)
										.setListener(
												new AnimatorListenerAdapter() {
													@Override
													public void onAnimationEnd(
															Animator animation) {
														super.onAnimationEnd(animation);
														mLoginView
																.setVisibility(View.INVISIBLE);
													}
												});

								mPasswordView
										.animate()
										.setDuration(60)
										.alpha(1)
										.setListener(
												new AnimatorListenerAdapter() {
													@Override
													public void onAnimationEnd(
															Animator animation) {
														super.onAnimationEnd(animation);
														mPasswordView
																.setVisibility(View.INVISIBLE);
													}
												});

								findViewById(R.id.bt_sign_in)
										.animate()
										.setDuration(80)
										.alpha(1)
										.setListener(
												new AnimatorListenerAdapter() {
													@Override
													public void onAnimationEnd(
															Animator animation) {
														super.onAnimationEnd(animation);
														findViewById(
																R.id.bt_sign_in)
																.setVisibility(
																		View.INVISIBLE);
													}
												});

								mCheckBox
										.animate()
										.setDuration(100)
										.alpha(1)
										.setListener(
												new AnimatorListenerAdapter() {
													@Override
													public void onAnimationEnd(
															Animator animation) {
														super.onAnimationEnd(animation);
														mCheckBox
																.setVisibility(View.INVISIBLE);

														mCheckBox
																.animate()
																.setDuration(20)
																.alpha(1)
																.setListener(
																		new AnimatorListenerAdapter() {
																			@Override
																			public void onAnimationEnd(
																					Animator animation) {
																				super.onAnimationEnd(animation);
																				mLoginView
																						.setVisibility(View.VISIBLE);

																				mPasswordView
																						.setVisibility(View.VISIBLE);

																				findViewById(
																						R.id.bt_sign_in)
																						.setVisibility(
																								View.VISIBLE);

																				mCheckBox
																						.setVisibility(View.VISIBLE);

																				/*
																				 * if
																				 * (
																				 * hasNavigationBar
																				 * (
																				 * )
																				 * )
																				 * mSystemUiHider
																				 * .
																				 * show
																				 * (
																				 * )
																				 * ;
																				 */

																				recreate();

																			}
																		});
													}
												});
							}
						}
					});

		} else {

			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
				com.actionbarsherlock.app.ActionBar ab = getSupportActionBar();
				ab.hide();
			}

			((TextView) findViewById(R.id.space_object))
					.setVisibility(View.VISIBLE);
			mLoginView.setVisibility(View.VISIBLE);
			mPasswordView.setVisibility(View.VISIBLE);
			((Button) findViewById(R.id.bt_sign_in))
					.setVisibility(View.VISIBLE);
			mCheckBox.setVisibility(View.VISIBLE);

		}

		/*
		 * String[] sql_create_entries = new String[] {
		 * UsersRepository.CREATE_TABLE_USER[0],
		 * CanteensRepository.CREATE_TABLE_CANTEEN[0],
		 * MealsRepository.CREATE_TABLE_MEAL[0],
		 * MealsRepository.CREATE_TABLE_MEAL[1],
		 * MealsRepository.CREATE_TABLE_MEAL[2],
		 * DishsRepository.CREATE_TABLE_DISH[0],
		 * ReferencesRepository.CREATE_TABLE_REFERENCE[0],
		 * ReservesRepository.CREATE_TABLE_RESERVE[0],
		 * ReservesRepository.CREATE_TABLE_RESERVE[1] };
		 * 
		 * String[] sql_delete_entries = new String[] {
		 * UsersRepository.DELETE_TABLE_USER[0],
		 * CanteensRepository.DELETE_TABLE_CANTEEN[0],
		 * MealsRepository.DELETE_TABLE_MEAL[0],
		 * MealsRepository.DELETE_TABLE_MEAL[1],
		 * MealsRepository.DELETE_TABLE_MEAL[2],
		 * DishsRepository.DELETE_TABLE_DISH[0],
		 * ReferencesRepository.DELETE_TABLE_REFERENCE[0],
		 * ReservesRepository.DELETE_TABLE_RESERVE[0],
		 * ReservesRepository.DELETE_TABLE_RESERVE[1] };
		 * 
		 * CantinaIplRepository cantinaIplDBRepository = new
		 * CantinaIplRepository( getApplicationContext(), false,
		 * sql_create_entries, sql_delete_entries);
		 * cantinaIplDBRepository.open(); cantinaIplDBRepository.close();
		 * 
		 * new UpdateApplication(this).executeOnExecutor(super.getExec(),
		 * "2091112$");
		 */

	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) {
	 * super.onCreateOptionsMenu(menu);
	 * getMenuInflater().inflate(R.menu.authentication, menu); return true; }
	 */

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.authentication, menu);
		return true;
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mLoginView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mLogin = mLoginView.getText().toString();
		mPassword = mPasswordView.getText().toString();
		mMemorized = ((CheckBox) findViewById(R.id.cb_memorize)).isChecked();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid login.
		if (TextUtils.isEmpty(mLogin)) {
			mLoginView.setError(getString(R.string.error_login_required));
			focusView = mLoginView;
			cancel = true;
		}

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_password_required));
			focusView = mPasswordView;
			cancel = true;
		} /*
		 * else if (mPassword.length() < 4) {
		 * mPasswordView.setError(getString(R.string.error_invalid_password));
		 * focusView = mPasswordView; cancel = true; }
		 */

		/*
		 * // Check for a valid email address. if (TextUtils.isEmpty(mEmail)) {
		 * mEmailView.setError(getString(R.string.error_field_required));
		 * focusView = mEmailView; cancel = true; } else if
		 * (!mEmail.contains("@")) {
		 * mEmailView.setError(getString(R.string.error_invalid_email));
		 * focusView = mEmailView; cancel = true; }
		 */

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			mAuthTask = new AuthenticationTask();
			mAuthTask.execute((Void) null);

			/*
			 * BaseActivity.getOldExec().executeOnExecutor( (AsyncTask<Void,
			 * Void, Authentication>) mAuthTask, (Object[]) null);
			 */
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	private void createSharedPreferences() {
		SharedPreferences.Editor prefsEditor = appPrefs.edit();
		String mLogin_cyf = mLogin;
		String mPassword_cyf = mPassword;
		try {
			mLogin_cyf = SimpleCrypto.encrypt("ipleiria-secret", mLogin);
			mPassword_cyf = SimpleCrypto.encrypt("ipleiria-secret", mPassword);
		} catch (Exception e) {
			Log.w("AUTH", e.getLocalizedMessage());
		}
		prefsEditor.putString("Login", mLogin_cyf);
		prefsEditor.putString("Password", mPassword_cyf);
		prefsEditor.putBoolean("Memorized", mMemorized);
		prefsEditor.commit();
	}

	private SharedPreference getPreferenceData() {
		String mLogin_cyf = appPrefs.getString("Login", "");
		String mPassword_cyf = appPrefs.getString("Password", "");
		try {
			mLogin_cyf = SimpleCrypto.decrypt("ipleiria-secret",
					appPrefs.getString("Login", ""));
			mPassword_cyf = SimpleCrypto.decrypt("ipleiria-secret",
					appPrefs.getString("Password", ""));
		} catch (Exception e) {
			Log.w("AUTH", e.getLocalizedMessage());
		}
		return new SharedPreference(mLogin_cyf, mPassword_cyf,
				appPrefs.getBoolean("Memorized", false));
	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private boolean hasNavigationBar() {

		boolean hasMenuKey = ViewConfiguration.get(getApplicationContext())
				.hasPermanentMenuKey();
		boolean hasBackKey = KeyCharacterMap
				.deviceHasKey(KeyEvent.KEYCODE_BACK);

		return ((!hasMenuKey && !hasBackKey) ? true : false);
	}

	// [REGION] Thread

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class AuthenticationTask extends
			AsyncTask<Void, Void, Authentication> {

		private static final String TAG = "AUTH";
		private static final String APP_PASSWORD = "ipl.cantina.1213@gmail.com";
		private static final String SERVICE_URL = "http://192.168.79.128/CantinaIplService.svc/UserAuthentication/";

		private CantinaIplRepository cantinaIplDBRepository;

		@Override
		protected Authentication doInBackground(Void... params) {

			if (((CantinaIplApplication) getApplicationContext())
					.isNetworkAvailable()) {
				try {

					String data = readJsonData(SERVICE_URL + mLogin + "$"
							+ mPassword + "$" + APP_PASSWORD);

					JSONObject jsonObject = new JSONObject(data);
					if (jsonObject != null) {
						try {
							JSONObject userObject = jsonObject
									.getJSONObject("User");

							if (userObject != null)
								UserSingleton.getInstance().user = new User(
										userObject.getString("Login"),
										userObject.getInt("Bi"),
										userObject.getString("Name"),
										userObject.getString("Course"),
										userObject.getBoolean("Regime"),
										userObject.getString("Photo"),
										userObject.getInt("Nif"),
										userObject.getString("Email"),
										userObject.getBoolean("Type"),
										userObject.getBoolean("Active"),
										userObject.getString("School"));
						} catch (Exception e) {
							Log.w(TAG, e.getLocalizedMessage());
							return null;
						}

						return new Authentication(UserSingleton.getInstance()
								.getUser() != null ? true : false,
								jsonObject.getBoolean("IsFirstLogin"),
								jsonObject.getString("Message"));
					}
				} catch (Exception e) {
					Log.w(TAG, e.getLocalizedMessage());
				}
			}
			return null;
		}

		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(final Authentication authentication) {
			mAuthTask = null;
			showProgress(false);

			if (!(authentication == null)) {
				if (authentication.isUserExist()) {
					createCacheDB();

					if (!authentication.isFirstLogin()) {
						UsersRepository usersRepository = new UsersRepository(
								getApplicationContext(), false);
						usersRepository.open();
						usersRepository.insertUser(UserSingleton.getInstance()
								.getUser());
						usersRepository.close();
						Intent intent = new Intent(getApplicationContext(),
								CanteensActivity.class);
						startActivity(intent);
					} else {
						Bundle bundle = new Bundle();
						bundle.putInt("user_login", Integer.parseInt(mLogin));
						Intent intent = new Intent(getApplicationContext(),
								UserActivity.class);
						intent.putExtra("user_extras", bundle);
						startActivity(intent);
					}

					finish();
				} else {
					mPasswordView
							.setError(getString(R.string.error_incorrect_password));
					mPasswordView.requestFocus();
				}
			} else {
				if (!((CantinaIplApplication) getApplicationContext())
						.isNetworkAvailable()) {
					DisplayToast("Problema na conexão à internet!");

					showDialog(5);
					/*
					 * Intent intent = new Intent(Intent.ACTION_MAIN);
					 * intent.setClassName("com.android.phone",
					 * "com.android.phone.NetworkSetting");
					 * startActivity(intent);
					 */
				} else {
					DisplayToast("Não é possível validar as credênciais!");
				}
			}

		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}

		private String readJsonData(String serviceUrl) {
			StringBuilder stringBuilder = new StringBuilder();
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(serviceUrl);

			try {
				HttpResponse response = httpClient.execute(httpGet);
				StatusLine statusLine = response.getStatusLine();
				int statusCode = statusLine.getStatusCode();
				if (statusCode == 200) {
					HttpEntity httpEntity = response.getEntity();
					InputStream content = httpEntity.getContent();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(content));
					String line;
					while ((line = reader.readLine()) != null) {
						stringBuilder.append(line);
					}
				} else {
					Log.e(TAG, "Failed to get Json data file.");
				}
			} catch (ClientProtocolException e) {
				Log.w(TAG, e.getLocalizedMessage());
			} catch (IOException e) {
				Log.w(TAG, e.getLocalizedMessage());
			}

			return stringBuilder.toString();
		}

		private void createCacheDB() {

			createSharedPreferences();

			String[] sql_create_entries = new String[] {
					UsersRepository.CREATE_TABLE_USER[0],
					CanteensRepository.CREATE_TABLE_CANTEEN[0],
					MealsRepository.CREATE_TABLE_MEAL[0],
					MealsRepository.CREATE_TABLE_MEAL[1],
					MealsRepository.CREATE_TABLE_MEAL[2],
					DishsRepository.CREATE_TABLE_DISH[0],
					ReferencesRepository.CREATE_TABLE_REFERENCE[0],
					ReservesRepository.CREATE_TABLE_RESERVE[0],
					ReservesRepository.CREATE_TABLE_RESERVE[1] };

			String[] sql_delete_entries = new String[] {
					UsersRepository.DELETE_TABLE_USER[0],
					CanteensRepository.DELETE_TABLE_CANTEEN[0],
					MealsRepository.DELETE_TABLE_MEAL[0],
					MealsRepository.DELETE_TABLE_MEAL[1],
					MealsRepository.DELETE_TABLE_MEAL[2],
					DishsRepository.DELETE_TABLE_DISH[0],
					ReferencesRepository.DELETE_TABLE_REFERENCE[0],
					ReservesRepository.DELETE_TABLE_RESERVE[0],
					ReservesRepository.DELETE_TABLE_RESERVE[1] };

			cantinaIplDBRepository = new CantinaIplRepository(
					getApplicationContext(), false, sql_create_entries,
					sql_delete_entries);
			cantinaIplDBRepository.open();
			cantinaIplDBRepository.close();

		}

	}

	// [ENDREGION] Thread

}
