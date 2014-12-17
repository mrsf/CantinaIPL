package pt.ipleiria.sas.mobile.cantinaipl.task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import pt.ipleiria.sas.mobile.cantinaipl.controller.SearchListAdapter;
import pt.ipleiria.sas.mobile.cantinaipl.controller.UserSingleton;
import pt.ipleiria.sas.mobile.cantinaipl.database.MealsRepository;
import pt.ipleiria.sas.mobile.cantinaipl.model.Dish;
import pt.ipleiria.sas.mobile.cantinaipl.model.Meal;

public class MealsSearch extends DataLoading<String, Meal, LinkedList<Meal>> {

	// [REGION] Constants

	private static final String TAG = "MEALS_SEARCH";
	private static final String SERVICE_METHOD = getServiceUrl()
			+ "SearchMeal/";

	// [ENDREGION] Constants

	// [REGION] Variables

	private final SearchListAdapter mealListAdapter;

	private LinkedList<Meal> mealList;
	private MealsRepository mealsRepository;
	
	private TextView listCount;

	// [ENDREGION] Variables

	// [REGION] Constructors

	public MealsSearch(final Context context, LinkedList<Meal> mealList,
			SearchListAdapter mealListAdapter, TextView listCount) {
		super(context);
		this.mealList = mealList;
		this.mealListAdapter = mealListAdapter;
		this.mealsRepository = new MealsRepository(super.getContext(), false);
		this.listCount = listCount;
	}

	// [ENDREGION] Constructors

	// [REGION] Inherited_Methods

	@Override
	protected LinkedList<Meal> doInBackground(String... params) {

		LinkedList<Meal> storedData = new LinkedList<Meal>();

		for (String param : params) {

			mealsRepository.open();
			storedData = mealsRepository.getMealsByName(param);
			mealsRepository.close();

			if (storedData.isEmpty() && super.isNetworkAvailable()) {

				try {
					JSONArray jsonArray = new JSONArray(
							readJsonData(SERVICE_METHOD + param + "$"
									+ super.getServiceAppPassword()));
					Log.i(TAG, "Number of Meal elements: " + jsonArray.length());

					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						List<Dish> dishList = new ArrayList<Dish>();
						JSONArray jsonDishArray = jsonObject
								.getJSONArray("Dishes");
						Log.i(TAG,
								"Number of dish elements: "
										+ jsonDishArray.length());

						for (int j = 0; j < jsonDishArray.length(); j++) {
							JSONObject jsonDishObject = jsonDishArray
									.getJSONObject(j);

							String photo = ((jsonDishObject.getString("Photo")
									.equals("null")) ? jsonDishObject
									.getString("Photo") : super.getServerUrl()
									+ jsonDishObject.getString("Photo"));

							dishList.add(new Dish(jsonDishObject.getInt("Id"),
									photo, jsonDishObject
											.getString("Description"),
									jsonDishObject.getString("Name"),
									jsonDishObject.getDouble("Price"),
									jsonDishObject.getString("Type"),
									jsonDishObject.getDouble("MyRating")));
						}

						Meal meal = new Meal(jsonObject.getInt("Id"),
								jsonObject.getString("Date"),
								jsonObject.getBoolean("Refeicao"),
								jsonObject.getString("Type"), dishList,
								(!UserSingleton.getInstance().getUser()
										.getType() ? jsonObject
										.getDouble("StudentPrice") : jsonObject
										.getDouble("EmployeePrice")));

						this.publishProgress(meal);
					}
				} catch (JSONException e) {
					Log.d(TAG, e.getLocalizedMessage());
					return null;
				}

			} else if (!storedData.isEmpty()) {

				Log.i(TAG, "Database has stored data!");

				for (Meal meal : storedData)
					publishProgress(meal);

			} else if (!this.isNetworkAvailable()) {
				Log.w(TAG, "Network is unavaiable!");
				return null;

			}

		}

		return (storedData.isEmpty() ? mealList : new LinkedList<Meal>());
	}

	@Override
	protected void onProgressUpdate(Meal... meals) {
		super.onProgressUpdate(meals);

		for (Meal meal : meals)
			this.mealList.add(meal);
	}

	@Override
	protected void onPostExecute(LinkedList<Meal> mealsResult) {
		super.onPostExecute(mealsResult);

		if (mealsResult != null) {
			this.mealListAdapter.notifyDataSetChanged();
			listCount.setText(String.valueOf(mealListAdapter.getCount()) + " "
					+ listCount.getText());
		}
	}

	// [ENDREGION] Inherited_Methods

}
