package pt.ipleiria.sas.mobile.cantinaipl.database;

import java.util.LinkedList;
import java.util.List;

import pt.ipleiria.sas.mobile.cantinaipl.model.Dish;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author Márcio Francisco and Mário Correia
 * @version 2013.0607
 * @since 1.0
 * 
 */
class MealDishRelation {

	// [REGION] SQL_Statements

	static final String CREATE_TABLE_MEALDISH = "CREATE TABLE IF NOT EXISTS "
			+ CantinaIplDBContract.MealDishBase.TABLE_NAME + " ("
			+ CantinaIplDBContract.MealDishBase.DISH_ID
			+ CantinaIplDBContract.INTEGER_TYPE
			+ CantinaIplDBContract.COMMA_SEP
			+ CantinaIplDBContract.MealDishBase.MEAL_ID
			+ CantinaIplDBContract.INTEGER_TYPE
			+ CantinaIplDBContract.COMMA_SEP + CantinaIplDBContract.PRIMARY_KEY
			+ " (" + CantinaIplDBContract.MealDishBase.DISH_ID
			+ CantinaIplDBContract.COMMA_SEP
			+ CantinaIplDBContract.MealDishBase.MEAL_ID + ") )";

	static final String DELETE_TABLE_MEALDISH = "DROP TABLE IF EXISTS "
			+ CantinaIplDBContract.MealDishBase.TABLE_NAME;

	// [ENDREGION] SQL_Statements

	// Prevents the MealDishRelation class from being instantiated.
	// [REGION] Constructor

	private MealDishRelation() {
	}

	// [ENDREGION] Constructor

	// [REGION] Private_Methods

	private static Cursor getRecord(int dishId, int mealId,
			SQLiteDatabase database) {

		Cursor cursor = database.rawQuery("SELECT * FROM "
				+ CantinaIplDBContract.MealDishBase.TABLE_NAME + " WHERE "
				+ CantinaIplDBContract.MealDishBase.DISH_ID + " = " + dishId
				+ " AND " + CantinaIplDBContract.MealDishBase.MEAL_ID + " = "
				+ mealId, null);

		if (cursor != null) {
			cursor.moveToFirst();
		}

		return cursor;
	}

	// [ENDREGION] Private_Methods

	// [REGION] Package_Accessible_Methods

	static List<Dish> getDishsByNameAndMealId(String name, int mealId,
			SQLiteDatabase database) {

		LinkedList<Dish> dishList = new LinkedList<Dish>();

		Cursor cursor = database.rawQuery("SELECT d.* FROM "
				+ CantinaIplDBContract.DishBase.TABLE_NAME + " d INNER JOIN "
				+ CantinaIplDBContract.MealDishBase.TABLE_NAME + " md ON d."
				+ CantinaIplDBContract.DishBase.DISH_ID + " = md."
				+ CantinaIplDBContract.MealDishBase.DISH_ID + " WHERE md."
				+ CantinaIplDBContract.MealDishBase.MEAL_ID + " = " + mealId,
				null);

		if (cursor != null) {
			cursor.moveToFirst();
		}

		if (cursor.moveToFirst()) {
			do {
				if (!cursor.getString(3).contains(name))
					continue;
				else
					dishList.add(new Dish(
							Integer.parseInt(cursor.getString(0)), cursor
									.getString(1), cursor.getString(2), cursor
									.getString(3), cursor.getDouble(4), cursor
									.getString(5), cursor.getDouble(6)));
			} while (cursor.moveToNext());
		}

		return dishList;
	}

	/**
	 * <b>Package accessible method to get dishes.</b>
	 * 
	 * <p>
	 * This method allow get all dishes associated to a meal id.
	 * </p>
	 * 
	 * @param mealId
	 * @param database
	 * @return A list with all dishes of meal.
	 */
	static List<Dish> getDishesByMealId(int mealId, SQLiteDatabase database) {

		LinkedList<Dish> dishList = new LinkedList<Dish>();

		Cursor cursor = database.rawQuery("SELECT d.* FROM "
				+ CantinaIplDBContract.DishBase.TABLE_NAME + " d INNER JOIN "
				+ CantinaIplDBContract.MealDishBase.TABLE_NAME + " md ON d."
				+ CantinaIplDBContract.DishBase.DISH_ID + " = md."
				+ CantinaIplDBContract.MealDishBase.DISH_ID + " WHERE md."
				+ CantinaIplDBContract.MealDishBase.MEAL_ID + " = " + mealId,
				null);

		if (cursor != null) {
			cursor.moveToFirst();
		}

		if (cursor.moveToFirst()) {
			do {
				dishList.add(new Dish(Integer.parseInt(cursor.getString(0)),
						cursor.getString(1), cursor.getString(2), cursor
								.getString(3), cursor.getDouble(4), cursor
								.getString(5), cursor.getDouble(6)));
			} while (cursor.moveToNext());
		}

		cursor.close();

		return dishList;
	}

	static long insertDishByMealId(Dish dish, int mealId,
			SQLiteDatabase database) {

		if (DishsRepository.insertDish(dish, database) == -1)
			return -1;

		if (getRecord(dish.getId(), mealId, database).getCount() == 0) {
			ContentValues values = new ContentValues();
			values.put(CantinaIplDBContract.MealDishBase.DISH_ID, dish.getId());
			values.put(CantinaIplDBContract.MealDishBase.MEAL_ID, mealId);
			return database.insert(
					CantinaIplDBContract.MealDishBase.TABLE_NAME, null, values);
		}

		return 0;
	}

	// [ENDREGION] Package_Accessible_Methods

}
