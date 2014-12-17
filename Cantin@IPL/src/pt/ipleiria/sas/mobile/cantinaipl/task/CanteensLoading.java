package pt.ipleiria.sas.mobile.cantinaipl.task;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

import pt.ipleiria.sas.mobile.cantinaipl.controller.CanteenListAdapter;
import pt.ipleiria.sas.mobile.cantinaipl.controller.UserSingleton;
import pt.ipleiria.sas.mobile.cantinaipl.database.CanteensRepository;
import pt.ipleiria.sas.mobile.cantinaipl.model.Canteen;
import pt.ipleiria.sas.mobile.cantinaipl.model.Dish;
import pt.ipleiria.sas.mobile.cantinaipl.model.Meal;

import android.content.Context;
import android.util.Log;

/**
 * Class to loading the canteens data.
 * 
 * This class allows canteens data loading. It can request data to web service
 * or read data stored on application database.
 * 
 * @author Márcio Francisco and Mário Correia
 * @version 2013.0606
 * @since 1.0
 * 
 */
public class CanteensLoading extends
		DataLoading<String, Canteen, LinkedList<Canteen>> {

	// [REGION] Constants

	private static final String TAG = "CANTEENS_LOADING";
	private static final String SERVICE_METHOD = getServiceUrl()
			+ "/GetCanteens";

	// [ENDREGION] Constants

	// [REGION] Variables

	private final CanteenListAdapter canteenListAdapter;

	private LinkedList<Canteen> canteenList;
	private LinkedList<Meal> mealList;
	private LinkedList<Dish> dishList;
	private CanteensRepository canteensRepository;

	// [ENDREGION] Variables

	// [REGION] Constructors

	public CanteensLoading(final Context context,
			final LinkedList<Canteen> canteenList,
			CanteenListAdapter canteenListAdapter) {
		super(context);
		this.canteenList = canteenList;
		this.canteenListAdapter = canteenListAdapter;
		this.canteensRepository = new CanteensRepository(super.getContext(),
				false);
	}

	// [ENDREGION] Constructors

	// [REGION] Inherited_Methods

	@Override
	protected LinkedList<Canteen> doInBackground(String... params) {

		LinkedList<Canteen> storedData = new LinkedList<Canteen>();

		for (String param : params) {

			this.canteensRepository.open();
			storedData = this.canteensRepository.getCanteens();
			this.canteensRepository.close();

			if (storedData.isEmpty() && super.isNetworkAvailable()) {

				try {
					JSONArray jsonArray = new JSONArray(
							readJsonData(SERVICE_METHOD + param));
					Log.i(TAG,
							"Number of canteen elements: " + jsonArray.length());

					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						this.mealList = new LinkedList<Meal>();
						JSONArray jsonMealArray = jsonObject
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
						
						String photo = ((jsonObject.getString("Photo")
								.equals("null")) ? jsonObject
								.getString("Photo") : super.getServerUrl()
								+ jsonObject.getString("Photo"));

						this.publishProgress(new Canteen(jsonObject
								.getInt("Id"), jsonObject.getString("Name"),
								jsonObject.getString("Address"), jsonObject
										.getString("AmPeriod"), jsonObject
										.getString("PmPeriod"), jsonObject
										.getString("Campus"), photo, jsonObject
										.getDouble("Latitude"), jsonObject
										.getDouble("Longitude"), jsonObject
										.getBoolean("Active"), new LinkedList<Meal>()));
					}
				} catch (Exception e) {
					Log.d(TAG, e.getLocalizedMessage());
					return null;
				}

			} else if (!storedData.isEmpty()) {

				Log.i(TAG, "Database has stored data!");

				for (Canteen canteen : storedData)
					this.publishProgress(canteen);

			} else if (!this.isNetworkAvailable()) {

				Log.w(TAG, "Network is unavaiable!");
				return null;

			}

		}

		return storedData.isEmpty() ? this.canteenList
				: new LinkedList<Canteen>();
	}

	@Override
	protected void onProgressUpdate(Canteen... canteens) {
		super.onProgressUpdate(canteens);

		for (Canteen canteen : canteens)
			this.canteenList.add(canteen);
	}

	@Override
	protected void onPostExecute(LinkedList<Canteen> canteensResult) {
		super.onPostExecute(canteensResult);

		this.canteenListAdapter.notifyDataSetChanged();

		if (canteensResult != null && !canteensResult.isEmpty()) {
			this.canteensRepository.open();
			this.canteensRepository.insertCanteens(canteensResult);
			this.canteensRepository.close();
		}
	}

	// [ENDREGION] Inherited_Methods

}
