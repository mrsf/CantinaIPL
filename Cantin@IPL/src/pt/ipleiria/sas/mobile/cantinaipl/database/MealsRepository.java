package pt.ipleiria.sas.mobile.cantinaipl.database;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import pt.ipleiria.sas.mobile.cantinaipl.model.Dish;
import pt.ipleiria.sas.mobile.cantinaipl.model.Meal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

/**
 * @author Márcio Francisco and Mário Correia
 * @version 2013.0810
 * @since 1.0
 * 
 */
public class MealsRepository extends CantinaIplRepository {

	// [REGION] SQL_Statements

	public static final String[] CREATE_TABLE_MEAL = new String[] {
			"CREATE TABLE IF NOT EXISTS "
					+ CantinaIplDBContract.MealBase.TABLE_NAME + " ("
					+ CantinaIplDBContract.MealBase.MEAL_ID
					+ CantinaIplDBContract.INTEGER_TYPE
					+ CantinaIplDBContract.PRIMARY_KEY
					+ CantinaIplDBContract.COMMA_SEP
					+ CantinaIplDBContract.MealBase.DATE
					+ CantinaIplDBContract.TEXT_TYPE
					+ CantinaIplDBContract.COMMA_SEP
					+ CantinaIplDBContract.MealBase.REFEICAO
					+ CantinaIplDBContract.NUMERIC_TYPE
					+ CantinaIplDBContract.COMMA_SEP
					+ CantinaIplDBContract.MealBase.TYPE
					+ CantinaIplDBContract.TEXT_TYPE
					+ CantinaIplDBContract.COMMA_SEP
					+ CantinaIplDBContract.MealBase.PRICE
					+ CantinaIplDBContract.REAL_TYPE + ")",
			MealCanteenRelation.CREATE_TABLE_MEALCANTEEN,
			MealDishRelation.CREATE_TABLE_MEALDISH };

	public static final String[] DELETE_TABLE_MEAL = new String[] {
			"DROP TABLE IF EXISTS " + CantinaIplDBContract.MealBase.TABLE_NAME,
			MealCanteenRelation.DELETE_TABLE_MEALCANTEEN,
			MealDishRelation.DELETE_TABLE_MEALDISH };

	// [ENDREGION] SQL_Statements

	// [REGION] Constructors

	public MealsRepository(Context ctx, Boolean dbUpdate) {
		super(ctx, dbUpdate, CREATE_TABLE_MEAL, DELETE_TABLE_MEAL);
	}

	// [ENDREGION] Constructors

	// [REGION] Private_Methods

	private boolean mealExist(int mealId) {

		boolean exist = false;
		Cursor cursor = database().rawQuery(
				"SELECT * FROM " + CantinaIplDBContract.MealBase.TABLE_NAME
						+ " WHERE " + CantinaIplDBContract.MealBase.MEAL_ID
						+ " = ?", new String[] { String.valueOf(mealId) });

		if (cursor != null)
			if (cursor.moveToFirst()) {
				exist = (cursor.getCount() == 0 ? false : true);
				cursor.close();
			}

		return exist;
	}

	private long insertMeal(Meal meal) throws SQLException {

		for (Dish dish : meal.getDishes())
			if (MealDishRelation.insertDishByMealId(dish, meal.getId(),
					database()) == -1)
				return -1;
			else
				continue;

		ContentValues values = new ContentValues();
		values.put(CantinaIplDBContract.MealBase.MEAL_ID, meal.getId());
		values.put(CantinaIplDBContract.MealBase.DATE, meal.getDate());
		values.put(CantinaIplDBContract.MealBase.REFEICAO, meal.isRefeicao());
		values.put(CantinaIplDBContract.MealBase.TYPE, meal.getType());
		values.put(CantinaIplDBContract.MealBase.PRICE, meal.getPrice());
		return database().insert(CantinaIplDBContract.MealBase.TABLE_NAME,
				null, values);

	}

	// [ENDREGION] Private_Methods

	// [REGION] Public_Methods

	/**
	 * <b>Method to get the meals.</b>
	 * 
	 * <p>
	 * This method allows to get the meals associated to a canteenId and
	 * refeicao.
	 * </p>
	 * 
	 * @param canteenId
	 *            Id of canteen associated to a meal.
	 * @param refeicao
	 *            Define if a meal is lunch(0) or dinner(1).
	 * @return List with meals associated to canteenId and refeicao.
	 */
	public LinkedList<Meal> getMealsByCanteenIdAndRefeicao(int canteenId,
			int refeicao) {

		// list to store the meals
		LinkedList<Meal> meals = new LinkedList<Meal>();
		// initializing the cursor
		Cursor cursor = MealCanteenRelation.getMealsByCanteenIdAndRefeicao(
				canteenId, refeicao, database());

		// verify if a cursor is not null and can move to the first element
		if (cursor != null)
			if (cursor.moveToFirst())
				do {
					// list with all dishes of a meal
					LinkedList<Dish> dishes = (LinkedList<Dish>) MealDishRelation
							.getDishesByMealId(cursor.getInt(0), database());

					// sorting the dishes list
					Collections.sort(dishes, new Comparator<Dish>() {
						@Override
						public int compare(Dish lhs, Dish rhs) {
							return lhs.getType().compareTo(rhs.getType());
						};
					});

					meals.add(new Meal(cursor.getInt(0), cursor.getString(1),
							(cursor.getInt(2) == 0 ? false : true), cursor
									.getString(3), dishes, cursor.getDouble(4)));

				} while (cursor.moveToNext());

		// clear cursor resources
		cursor.close();

		return meals;
	}

	/**
	 * <b>Method to get the best meals.</b>
	 * 
	 * <p>
	 * This method allows to get the best meals associated to a rating.
	 * </p>
	 * 
	 * @param canteenId
	 *            Id of canteen associated to a meal.
	 * @param refeicao
	 *            Define if a meal is lunch(0) or dinner(1).
	 * @param rating
	 *            Rating of meals to get.
	 * @return List with best meals associated to rating.
	 */
	public LinkedList<Meal> getBestMealsByCanteenIdAndRefeicao(int canteenId,
			int refeicao, double rating) {

		// list to store the meals
		LinkedList<Meal> meals = new LinkedList<Meal>();
		// initializing the cursor
		Cursor cursor = MealCanteenRelation.getMealsByCanteenIdAndRefeicao(
				canteenId, refeicao, database());

		// verify if a cursor is not null and can move to the first element
		if (cursor != null)
			if (cursor.moveToFirst())
				do {
					// list with all dishes of a meal
					LinkedList<Dish> dishes = (LinkedList<Dish>) MealDishRelation
							.getDishesByMealId(cursor.getInt(0), database());

					// sorting the dishes list
					Collections.sort(dishes, new Comparator<Dish>() {
						@Override
						public int compare(Dish lhs, Dish rhs) {
							return lhs.getType().compareTo(rhs.getType());
						};
					});

					// verify the main dish rating
					if (dishes.get(0).getRating() >= rating)
						meals.add(new Meal(cursor.getInt(0), cursor
								.getString(1), (cursor.getInt(2) == 0 ? false
								: true), cursor.getString(3), dishes, cursor
								.getDouble(4)));

				} while (cursor.moveToNext());

		// clear cursor resources
		cursor.close();

		return meals;
	}

	/**
	 * <b>Method to get meals by name.</b>
	 * 
	 * <p>
	 * This method allows to get the meals by meal dish name specified.
	 * </p>
	 * 
	 * @param name
	 *            Name of meal dish to search
	 * @return A list with meals.
	 */
	public LinkedList<Meal> getMealsByName(String name) {

		// list to store the meals
		LinkedList<Meal> meals = new LinkedList<Meal>();
		// initializing the cursor
		Cursor cursor = database().rawQuery(
				"SELECT * FROM " + CantinaIplDBContract.MealBase.TABLE_NAME,
				null);

		// verify if a cursor is not null and can move to the first element
		if (cursor != null)
			if (cursor.moveToFirst())
				do {
					// list with all dishes of a meal
					LinkedList<Dish> dishes = (LinkedList<Dish>) MealDishRelation
							.getDishsByNameAndMealId(name, cursor.getInt(0),
									database());

					// verify if exists dishes in list
					if (dishes.isEmpty())
						continue;
					else
						meals.add(new Meal(cursor.getInt(0), cursor
								.getString(1), (cursor.getInt(2) == 0 ? false
								: true), cursor.getString(3), dishes, cursor
								.getDouble(4)));

				} while (cursor.moveToNext());

		// clear cursor resources
		cursor.close();

		return meals;
	}

	/**
	 * <b>Method to search a meal by your id.</b>
	 * 
	 * <p>
	 * This method allows a meal search by your's id.
	 * </p>
	 * 
	 * @param id
	 *            Id of meal to search.
	 * @return The meal with id specified.
	 */
	public Meal getMealById(int id) {

		// Object to store the meal
		Meal meal = null;
		// initializing the cursor
		Cursor cursor = database().rawQuery(
				"SELECT * FROM " + CantinaIplDBContract.MealBase.TABLE_NAME
						+ " WHERE " + CantinaIplDBContract.MealBase.MEAL_ID
						+ " = ?", new String[] { String.valueOf(id) });

		// verify if a cursor is not null and can move to the first element
		if (cursor != null)
			if (cursor.moveToFirst()) {
				// list with all dishes of a meal
				LinkedList<Dish> dishes = (LinkedList<Dish>) MealDishRelation
						.getDishesByMealId(cursor.getInt(0), database());

				meal = new Meal(cursor.getInt(0), cursor.getString(1),
						(cursor.getInt(2) == 0 ? false : true),
						cursor.getString(3), dishes, cursor.getDouble(4));
			}

		// clear cursor resources
		cursor.close();

		return meal;
	}

	/**
	 * <b>Method to insert a collection of meals.</b>
	 * 
	 * <p>
	 * This method allows meals storing associating to canteen.
	 * </p>
	 * 
	 * @param meals
	 *            List with meals to store on database.
	 * @param canteenId
	 *            Id of canteen associated to meals.
	 */
	public void insertMeals(LinkedList<Meal> meals, int canteenId) {

		for (Meal meal : meals)
			if (meal != null)
				if (!this.mealExist(meal.getId()))
					if (this.insertMeal(meal) != -1)
						MealCanteenRelation.insertMealByCanteenId(meal,
								canteenId, database());
	}

	// [ENDREGION] Public_Methods

}
