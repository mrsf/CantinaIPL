package pt.ipleiria.sas.mobile.cantinaipl.database;

import java.util.List;

import pt.ipleiria.sas.mobile.cantinaipl.model.Dish;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * <b>Reserved dishes data access class.</b>
 * 
 * <p>
 * This class allows reserved dishes data management in the application
 * database.
 * </p>
 * 
 * @author Márcio Francisco and Mário Correia
 * @version 2013.0620
 * @since 1.0
 * 
 */
class ReserveDishRelation {

	// [REGION] SQL_Statements

	static final String CREATE_TABLE_RESERVEDISH = "CREATE TABLE IF NOT EXISTS "
			+ CantinaIplDBContract.ReserveDishBase.TABLE_NAME
			+ " ("
			+ CantinaIplDBContract.ReserveDishBase.RESERVE_ID
			+ CantinaIplDBContract.INTEGER_TYPE
			+ CantinaIplDBContract.COMMA_SEP
			+ CantinaIplDBContract.ReserveDishBase.DISH_ID
			+ CantinaIplDBContract.INTEGER_TYPE
			+ CantinaIplDBContract.COMMA_SEP
			+ CantinaIplDBContract.PRIMARY_KEY
			+ " ("
			+ CantinaIplDBContract.ReserveDishBase.RESERVE_ID
			+ CantinaIplDBContract.COMMA_SEP
			+ CantinaIplDBContract.ReserveDishBase.DISH_ID + ") )";

	static final String DELETE_TABLE_RESERVEDISH = "DROP TABLE IF EXISTS "
			+ CantinaIplDBContract.ReserveDishBase.TABLE_NAME;

	// [ENDREGION] SQL_Statements

	// Prevents the ReserveDishRelation class from being instantiated.
	// [REGION] Constructor

	private ReserveDishRelation() {
	}

	// [ENDREGION] Constructor

	// [REGION] Private_Methods

	private static boolean reserveDishExist(int reserveId, int dishId,
			SQLiteDatabase database) {

		boolean exist = false;
		Cursor cursor = database
				.rawQuery(
						"SELECT * FROM "
								+ CantinaIplDBContract.ReserveDishBase.TABLE_NAME
								+ " WHERE "
								+ CantinaIplDBContract.ReserveDishBase.RESERVE_ID
								+ " = ? AND "
								+ CantinaIplDBContract.ReserveDishBase.DISH_ID
								+ " = ?",
						new String[] { String.valueOf(reserveId),
								String.valueOf(dishId) });

		if (cursor != null)
			cursor.moveToFirst();

		exist = (cursor.getCount() == 0 ? false : true);
		cursor.close();

		return exist;
	}

	// [ENDREGION] Private_Methods

	// [REGION] Package_Accessible_Methods

	static long insertReserveDishes(int reserveId, List<Dish> dishes,
			SQLiteDatabase database) {

		if (dishes != null) {
			ContentValues values = null;
			for (Dish dish : dishes)
				if (!reserveDishExist(reserveId, dish.getId(), database)) {
					values = new ContentValues();
					values.put(CantinaIplDBContract.ReserveDishBase.RESERVE_ID,
							reserveId);
					values.put(CantinaIplDBContract.ReserveDishBase.DISH_ID,
							dish.getId());
					if (database.insert(
							CantinaIplDBContract.ReserveDishBase.TABLE_NAME,
							null, values) == -1)
						return -1;
				}

			return 0;
		}

		return -1;
	}

	// [ENDREGION] Package_Accessible_Methods

}
