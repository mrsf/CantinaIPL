package pt.ipleiria.sas.mobile.cantinaipl.task;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pt.ipleiria.sas.mobile.cantinaipl.controller.UserSingleton;
import pt.ipleiria.sas.mobile.cantinaipl.database.CanteensRepository;
import pt.ipleiria.sas.mobile.cantinaipl.model.Canteen;
import pt.ipleiria.sas.mobile.cantinaipl.model.Dish;
import pt.ipleiria.sas.mobile.cantinaipl.model.Meal;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

public class UpdateApplication extends DataLoading<String, Object, Boolean> {

	// [REGION] Constants

	private static final String TAG = "UPDATE_APPLICATION";
	private static final String SERVICE_METHOD = getServiceUrl() + "UpdateApp/";

	// [ENDREGION] Constants

	private LinkedList<Canteen> canteenList;
	private LinkedList<Meal> mealList;
	private LinkedList<Dish> dishList;
	private CanteensRepository canteensRepository;
	private ProgressDialog progressDialog;

	public UpdateApplication(final Context context,
			ProgressDialog progressDialog) {
		super(context);
		this.canteenList = null;
		this.mealList = null;
		this.dishList = null;
		this.canteensRepository = new CanteensRepository(super.getContext(),
				false);
		this.progressDialog = progressDialog;
	}

	@Override
	protected Boolean doInBackground(String... params) {

		for (String param : params) {

			if (super.isNetworkAvailable()) {

				try {
					JSONArray jsonArray = new JSONArray(
							readJsonData(SERVICE_METHOD + param
									+ super.getServiceAppPassword()));
					Log.i(TAG, "Loading data of update...");

					for (int i = 0; i < jsonArray.length(); i++) {

						JSONObject jsonObjectI = jsonArray.getJSONObject(i);
						this.canteenList = new LinkedList<Canteen>();
						JSONArray jsonCanteenArray = jsonObjectI
								.getJSONArray("Canteens");
						Log.i(TAG, "Number of canteen elements: "
								+ jsonCanteenArray.length());

						for (int j = 0; j < jsonCanteenArray.length(); j++) {

							JSONObject jsonCanteenObject = jsonCanteenArray
									.getJSONObject(j);
							this.mealList = new LinkedList<Meal>();
							JSONArray jsonMealArray = jsonCanteenObject
									.getJSONArray("Meals");
							Log.i(TAG, "Number of meals elements: "
									+ jsonMealArray.length());

							for (int l = 0; l < jsonMealArray.length(); l++) {

								JSONObject jsonMealObject = jsonMealArray
										.getJSONObject(l);
								this.dishList = new LinkedList<Dish>();
								JSONArray jsonDishArray = jsonMealObject
										.getJSONArray("Dishes");
								Log.i(TAG, "Number of dishes elements: "
										+ jsonDishArray.length());

								for (int k = 0; k < jsonDishArray.length(); k++) {

									JSONObject jsonDishObject = jsonDishArray
											.getJSONObject(k);

									String photo = ((jsonDishObject
											.getString("Photo").equals("null")) ? jsonDishObject
											.getString("Photo") : super
											.getServerUrl()
											+ jsonDishObject.getString("Photo"));

									this.dishList.add(new Dish(jsonDishObject
											.getInt("Id"), photo,
											jsonDishObject
													.getString("Description"),
											jsonDishObject.getString("Name"),
											jsonDishObject.getDouble("Price"),
											jsonDishObject.getString("Type"),
											jsonDishObject
													.getDouble("MyRating")));

								}

								this.mealList
										.add(new Meal(
												jsonMealObject.getInt("Id"),
												jsonMealObject
														.getString("Date"),
												jsonMealObject
														.getBoolean("Refeicao"),
												jsonMealObject
														.getString("Type"),
												dishList,
												(!UserSingleton.getInstance()
														.getUser().getType() ? jsonMealObject
														.getDouble("StudentPrice")
														: jsonMealObject
																.getDouble("EmployeePrice"))));

							}

							String photo = ((jsonCanteenObject
									.getString("Photo").equals("null")) ? jsonCanteenObject
									.getString("Photo") : super.getServerUrl()
									+ jsonCanteenObject.getString("Photo"));

							this.canteenList.add(new Canteen(jsonCanteenObject
									.getInt("Id"), jsonCanteenObject
									.getString("Name"), jsonCanteenObject
									.getString("Address"), jsonCanteenObject
									.getString("AmPeriod"), jsonCanteenObject
									.getString("PmPeriod"), jsonCanteenObject
									.getString("Campus"), photo,
									jsonCanteenObject.getDouble("Latitude"),
									jsonCanteenObject.getDouble("Longitude"),
									jsonCanteenObject.getBoolean("Active"),
									mealList));

						}

						this.canteensRepository.open();
						this.canteensRepository
								.insertCanteens(this.canteenList);
						this.canteensRepository.close();

					}
				} catch (JSONException e) {
					Log.d(TAG, e.getLocalizedMessage());
					return false;
				}

			} else if (!this.isNetworkAvailable()) {
				Log.w(TAG, "Network is unavaiable!");
				return false;

			}

		}

		return true;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		if (result)
			Log.i(TAG, "App updated");
		
		this.progressDialog.dismiss();
	}

}
