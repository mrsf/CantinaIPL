package pt.ipleiria.sas.mobile.cantinaipl.task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pt.ipleiria.sas.mobile.cantinaipl.controller.MealListAdapter;
import pt.ipleiria.sas.mobile.cantinaipl.controller.UserSingleton;
import pt.ipleiria.sas.mobile.cantinaipl.database.MealsRepository;
import pt.ipleiria.sas.mobile.cantinaipl.model.Dish;
import pt.ipleiria.sas.mobile.cantinaipl.model.Meal;

import android.content.Context;
import android.util.Log;

/**
 * <b>Class to loading the meals data.</b>
 * 
 * <p>
 * This class allows meals data loading. It can request data to web service or
 * read data stored on application database.
 * </p>
 * 
 * @author Márcio Francisco and Mário Correia
 * @version 2013.0610
 * @since 1.0
 * 
 */
public class MealsLoading extends DataLoading<String, Meal, LinkedList<Meal>> {

	// [REGION] Constants

	// private static final Object LOCK = new Object();
	private static final String TAG = "MEALS_LOADING";
	private static final String SERVICE_METHOD = getServiceUrl()
			+ "/GetMealsByRefeicaoAndCanteenId";

	// [ENDREGION] Constants

	// [REGION] Variables

	private final MealListAdapter mealListAdapter;

	private LinkedList<Meal> mealList;
	private MealsRepository mealsRepository;

	private int canteenId;
	private int refeicao;

	// [ENDREGION] Variables

	// [REGION] Constructors

	public MealsLoading(final Context context, LinkedList<Meal> mealList,
			MealListAdapter mealListAdapter) {
		super(context);
		this.mealList = mealList;
		this.mealListAdapter = mealListAdapter;
		this.mealsRepository = new MealsRepository(super.getContext(), false);
	}

	// [ENDREGION] Constructors

	// [REGION] Inherited_Methods

	@Override
	protected LinkedList<Meal> doInBackground(String... params) {

		LinkedList<Meal> storedData = new LinkedList<Meal>();

		for (String param : params) {

			getServiceParams(param);

			// synchronized (LOCK) {
			this.mealsRepository.open();
			storedData = this.mealsRepository.getMealsByCanteenIdAndRefeicao(
					canteenId, refeicao);
			this.mealsRepository.close();
			// }

			if (storedData.isEmpty() && super.isNetworkAvailable()) {

				try {
					JSONArray jsonArray = new JSONArray(
							readJsonData(SERVICE_METHOD + param));
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
										.isType() ? jsonObject
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

			if (mealsResult != null && !mealsResult.isEmpty()) {
				// synchronized (LOCK) {
				this.mealsRepository.open();
				this.mealsRepository.insertMeals(mealsResult, canteenId);
				this.mealsRepository.close();
				// }
			}
		}
	}

	// [ENDREGION] Inherited_Methods

	// [REGION] Methods

	private void getServiceParams(String serviceParams) {

		String[] paramsArray = serviceParams.split("\\$");
		int position = paramsArray.length - 1;

		this.refeicao = (paramsArray[position - 1].equals("false") ? 0 : 1);
		this.canteenId = Integer.parseInt(paramsArray[position]);
	}

	// [ENDREGION] Methods

}
