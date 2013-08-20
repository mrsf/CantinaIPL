package pt.ipleiria.sas.mobile.cantinaipl.database;

import pt.ipleiria.sas.mobile.cantinaipl.model.Meal;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author Márcio Francisco and Mário Correia
 * @version 2013.0607
 * @since 1.0
 * 
 */
class MealCanteenRelation {

	// [REGION] SQL_Statements

	static final String CREATE_TABLE_MEALCANTEEN = "CREATE TABLE IF NOT EXISTS "
			+ CantinaIplDBContract.MealCanteenBase.TABLE_NAME
			+ " ("
			+ CantinaIplDBContract.MealCanteenBase.CANTEEN_ID
			+ CantinaIplDBContract.INTEGER_TYPE
			+ CantinaIplDBContract.COMMA_SEP
			+ CantinaIplDBContract.MealCanteenBase.MEAL_ID
			+ CantinaIplDBContract.INTEGER_TYPE
			+ CantinaIplDBContract.COMMA_SEP
			+ CantinaIplDBContract.PRIMARY_KEY
			+ " ("
			+ CantinaIplDBContract.MealCanteenBase.CANTEEN_ID
			+ CantinaIplDBContract.COMMA_SEP
			+ CantinaIplDBContract.MealCanteenBase.MEAL_ID + ") )";

	static final String DELETE_TABLE_MEALCANTEEN = "DROP TABLE IF EXISTS "
			+ CantinaIplDBContract.MealCanteenBase.TABLE_NAME;

	// [ENDREGION] SQL_Statements

	// Prevents the MealCanteenRelation class from being instantiated.
	// [REGION] Constructor

	private MealCanteenRelation() {
	}

	// [ENDREGION] Constructor

	// [REGION] Private_Methods

	private static Cursor getRecord(int canteenId, int mealId,
			SQLiteDatabase database) {

		Cursor cursor = database.rawQuery(
				"SELECT * FROM "
						+ CantinaIplDBContract.MealCanteenBase.TABLE_NAME
						+ " WHERE "
						+ CantinaIplDBContract.MealCanteenBase.CANTEEN_ID
						+ " = " + canteenId + " AND "
						+ CantinaIplDBContract.MealCanteenBase.MEAL_ID + " = "
						+ mealId, null);

		if (cursor != null) {
			cursor.moveToFirst();
		}

		return cursor;
	}

	// [ENDREGION] Private_Methods

	// [REGION] Package_Accessible_Methods

	static Cursor getMealsByCanteenIdAndRefeicao(int canteenId, int refeicao,
			SQLiteDatabase database) {

		Cursor cursor = database.rawQuery(
				"SELECT m.* FROM " + CantinaIplDBContract.MealBase.TABLE_NAME
						+ " m INNER JOIN "
						+ CantinaIplDBContract.MealCanteenBase.TABLE_NAME
						+ " mc ON m." + CantinaIplDBContract.MealBase.MEAL_ID
						+ " = mc."
						+ CantinaIplDBContract.MealCanteenBase.MEAL_ID
						+ " WHERE m." + CantinaIplDBContract.MealBase.REFEICAO
						+ " = ? AND mc."
						+ CantinaIplDBContract.MealCanteenBase.CANTEEN_ID
						+ " = ? AND",
				new String[] { String.valueOf(refeicao),
						String.valueOf(canteenId) });

		return cursor;
	}

	static long insertMealByCanteenId(Meal meal, int canteenId,
			SQLiteDatabase database) {

		if (getRecord(meal.getId(), canteenId, database).getCount() == 0) {
			ContentValues values = new ContentValues();
			values.put(CantinaIplDBContract.MealCanteenBase.CANTEEN_ID,
					canteenId);
			values.put(CantinaIplDBContract.MealCanteenBase.MEAL_ID,
					meal.getId());
			return database.insert(
					CantinaIplDBContract.MealCanteenBase.TABLE_NAME, null,
					values);
		}

		return 0;
	}

	// [ENDREGION] Package_Accessible_Methods

}
