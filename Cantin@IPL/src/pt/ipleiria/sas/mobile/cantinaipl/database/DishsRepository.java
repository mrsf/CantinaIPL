package pt.ipleiria.sas.mobile.cantinaipl.database;

import pt.ipleiria.sas.mobile.cantinaipl.model.Dish;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author Márcio Francisco and Mário Correia
 * @version 2013.0607
 * @since 1.0
 * 
 */
public class DishsRepository extends CantinaIplRepository {

	// [REGION] SQL_Statements

	public static final String[] CREATE_TABLE_DISH = new String[] { "CREATE TABLE IF NOT EXISTS "
			+ CantinaIplDBContract.DishBase.TABLE_NAME
			+ " ("
			+ CantinaIplDBContract.DishBase.DISH_ID
			+ CantinaIplDBContract.INTEGER_TYPE
			+ CantinaIplDBContract.PRIMARY_KEY
			+ CantinaIplDBContract.COMMA_SEP
			+ CantinaIplDBContract.DishBase.PHOTO
			+ CantinaIplDBContract.TEXT_TYPE
			+ CantinaIplDBContract.COMMA_SEP
			+ CantinaIplDBContract.DishBase.DESCRIPTION
			+ CantinaIplDBContract.TEXT_TYPE
			+ CantinaIplDBContract.COMMA_SEP
			+ CantinaIplDBContract.DishBase.NAME
			+ CantinaIplDBContract.TEXT_TYPE
			+ CantinaIplDBContract.COMMA_SEP
			+ CantinaIplDBContract.DishBase.PRICE
			+ CantinaIplDBContract.REAL_TYPE
			+ CantinaIplDBContract.COMMA_SEP
			+ CantinaIplDBContract.DishBase.TYPE
			+ CantinaIplDBContract.TEXT_TYPE
			+ CantinaIplDBContract.COMMA_SEP
			+ CantinaIplDBContract.DishBase.RATING
			+ CantinaIplDBContract.REAL_TYPE + ")" };

	public static final String[] DELETE_TABLE_DISH = new String[] { "DROP TABLE IF EXISTS "
			+ CantinaIplDBContract.DishBase.TABLE_NAME };

	// [ENDREGION] SQL_Statements

	// [REGION] Constructors

	public DishsRepository(Context ctx, Boolean dbUpdate) {
		super(ctx, dbUpdate, CREATE_TABLE_DISH, DELETE_TABLE_DISH);
	}

	// [ENDREGION] Constructors

	// [REGION] Private_Methods

	private static Cursor getDishById(int dishId, SQLiteDatabase database) {

		Cursor cursor = database.rawQuery("SELECT * FROM "
				+ CantinaIplDBContract.DishBase.TABLE_NAME + " WHERE "
				+ CantinaIplDBContract.DishBase.DISH_ID + " = " + dishId, null);

		if (cursor != null) {
			cursor.moveToFirst();
		}

		return cursor;
	}

	// [ENDREGION] Private_Methods

	// [REGION] Package_Accessible_Methods

	static long insertDish(Dish dish, SQLiteDatabase database)
			throws SQLException {

		if (getDishById(dish.getId(), database).getCount() == 0) {
			ContentValues values = new ContentValues();
			values.put(CantinaIplDBContract.DishBase.DISH_ID, dish.getId());
			values.put(CantinaIplDBContract.DishBase.PHOTO, dish.getPhoto());
			values.put(CantinaIplDBContract.DishBase.DESCRIPTION,
					dish.getDescription());
			values.put(CantinaIplDBContract.DishBase.NAME, dish.getName());
			values.put(CantinaIplDBContract.DishBase.PRICE, dish.getPrice());
			values.put(CantinaIplDBContract.DishBase.TYPE, dish.getType());
			values.put(CantinaIplDBContract.DishBase.RATING, dish.getRating());
			return database.insert(CantinaIplDBContract.DishBase.TABLE_NAME,
					null, values);
		}

		return 0;
	}

	// [ENDREGION] Package_Accessible_Methods

}
