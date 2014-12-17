package pt.ipleiria.sas.mobile.cantinaipl.task;

import org.json.JSONObject;

import pt.ipleiria.sas.mobile.cantinaipl.CanteensActivity;
import pt.ipleiria.sas.mobile.cantinaipl.CantinaIplApplication;
import pt.ipleiria.sas.mobile.cantinaipl.controller.UserSingleton;
import pt.ipleiria.sas.mobile.cantinaipl.database.CanteensRepository;
import pt.ipleiria.sas.mobile.cantinaipl.database.CantinaIplRepository;
import pt.ipleiria.sas.mobile.cantinaipl.database.DishsRepository;
import pt.ipleiria.sas.mobile.cantinaipl.database.MealsRepository;
import pt.ipleiria.sas.mobile.cantinaipl.database.ReferencesRepository;
import pt.ipleiria.sas.mobile.cantinaipl.database.ReservesRepository;
import pt.ipleiria.sas.mobile.cantinaipl.database.UsersRepository;
import pt.ipleiria.sas.mobile.cantinaipl.model.Authentication;
import pt.ipleiria.sas.mobile.cantinaipl.model.User;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Represents an asynchronous login/registration task used to authenticate the
 * user.
 */
public class UserAuthentication extends
		DataLoading<String, Void, Authentication> {

	private static final String TAG = "USER_AUTHENTICATION";
	private static final String SERVICE_METHOD = getServiceUrl()
			+ "UserAuthentication/";

	private CantinaIplRepository cantinaIplDBRepository;

	public UserAuthentication(final Context context) {
		super(context);
		this.cantinaIplDBRepository = null;
	}

	@Override
	protected Authentication doInBackground(String... params) {
		if (((CantinaIplApplication) super.getContext().getApplicationContext())
				.isNetworkAvailable()) {
			try {

				String data = super
						.readJsonData(SERVICE_METHOD + params[0] + "$"
								+ params[1] + "$"
								+ super.getServiceAppPassword());

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

					return new Authentication(
							jsonObject.getBoolean("UserExist"),
							jsonObject.getBoolean("IsFirstLogin"),
							jsonObject.getString("Message"));
				}
			} catch (Exception e) {
				Log.w(TAG, e.getLocalizedMessage());
			}
		}
		return null;
	}

	@Override
	protected void onPostExecute(final Authentication authentication) {
		// mAuthTask = null;
		// showProgress(false);

		if (!(authentication == null)) {
			if (authentication.isUserExist()) {
				createCacheDB();

				if (!authentication.isFirstLogin()) {
					UsersRepository usersRepository = new UsersRepository(super
							.getContext().getApplicationContext(), false);
					usersRepository.open();
					usersRepository.insertUser(UserSingleton.getInstance()
							.getUser());
					usersRepository.close();
					Intent intent = new Intent(super.getContext()
							.getApplicationContext(), CanteensActivity.class);
					super.getContext().startActivity(intent);
				} else {
					/*
					 * Bundle bundle = new Bundle(); bundle.putInt("user_login",
					 * Integer.parseInt(mLogin)); Intent intent = new
					 * Intent(super.getContext() .getApplicationContext(),
					 * UserActivity.class); intent.putExtra("user_extras",
					 * bundle); super.getContext().startActivity(intent);
					 */
				}

				// finish();
			} else {
				/*
				 * mPasswordView
				 * .setError(getString(R.string.error_incorrect_password));
				 * mPasswordView.requestFocus();
				 */
			}
		} else {
			if (!((CantinaIplApplication) super.getContext()
					.getApplicationContext()).isNetworkAvailable()) {
				/*
				 * DisplayToast("Problema na conexão à internet!");
				 * 
				 * showDialog(5);
				 */

				/*
				 * Intent intent = new Intent(Intent.ACTION_MAIN);
				 * intent.setClassName("com.android.phone",
				 * "com.android.phone.NetworkSetting"); startActivity(intent);
				 */
			} else {
				// DisplayToast("Não é possível validar as credênciais!");
			}
		}

	}

	@Override
	protected void onCancelled() {
		// mAuthTask = null;
		// showProgress(false);
	}

	private void createCacheDB() {

		// createSharedPreferences();

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

		cantinaIplDBRepository = new CantinaIplRepository(super.getContext()
				.getApplicationContext(), false, sql_create_entries,
				sql_delete_entries);
		cantinaIplDBRepository.open();
		cantinaIplDBRepository.close();

	}

}
