package pt.ipleiria.sas.mobile.cantinaipl.database;

import java.util.LinkedList;

import pt.ipleiria.sas.mobile.cantinaipl.model.Canteen;
import pt.ipleiria.sas.mobile.cantinaipl.model.Meal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

/**
 * @author Márcio Francisco and Mário Correia
 * @version 2013.0606
 * @since 1.0
 * 
 */
public class CanteensRepository extends CantinaIplRepository {

	// [REGION] SQL_Statements

	public static final String[] CREATE_TABLE_CANTEEN = new String[] { "CREATE TABLE IF NOT EXISTS "
			+ CantinaIplDBContract.CanteenBase.TABLE_NAME
			+ " ("
			+ CantinaIplDBContract.CanteenBase.CANTEEN_ID
			+ CantinaIplDBContract.INTEGER_TYPE
			+ CantinaIplDBContract.PRIMARY_KEY
			+ CantinaIplDBContract.COMMA_SEP
			+ CantinaIplDBContract.CanteenBase.NAME
			+ CantinaIplDBContract.TEXT_TYPE
			+ CantinaIplDBContract.COMMA_SEP
			+ CantinaIplDBContract.CanteenBase.ADDRESS
			+ CantinaIplDBContract.TEXT_TYPE
			+ CantinaIplDBContract.COMMA_SEP
			+ CantinaIplDBContract.CanteenBase.AM_PERIOD
			+ CantinaIplDBContract.TEXT_TYPE
			+ CantinaIplDBContract.COMMA_SEP
			+ CantinaIplDBContract.CanteenBase.PM_PERIOD
			+ CantinaIplDBContract.TEXT_TYPE
			+ CantinaIplDBContract.COMMA_SEP
			+ CantinaIplDBContract.CanteenBase.CAMPUS
			+ CantinaIplDBContract.TEXT_TYPE
			+ CantinaIplDBContract.COMMA_SEP
			+ CantinaIplDBContract.CanteenBase.PHOTO
			+ CantinaIplDBContract.TEXT_TYPE
			+ CantinaIplDBContract.COMMA_SEP
			+ CantinaIplDBContract.CanteenBase.LATITUDE
			+ CantinaIplDBContract.REAL_TYPE
			+ CantinaIplDBContract.COMMA_SEP
			+ CantinaIplDBContract.CanteenBase.LONGITUDE
			+ CantinaIplDBContract.REAL_TYPE
			+ CantinaIplDBContract.COMMA_SEP
			+ CantinaIplDBContract.CanteenBase.ACTIVE
			+ CantinaIplDBContract.NUMERIC_TYPE + ")" };

	public static final String[] DELETE_TABLE_CANTEEN = new String[] { "DROP TABLE IF EXISTS "
			+ CantinaIplDBContract.CanteenBase.TABLE_NAME };

	// [ENDREGION] SQL_Statements

	// [REGION] Constructors

	public CanteensRepository(Context ctx, Boolean dbUpdate) {
		super(ctx, dbUpdate, CREATE_TABLE_CANTEEN, DELETE_TABLE_CANTEEN);
	}

	// [ENDREGION] Constructors

	// [REGION] Private_Methods

	private Cursor getCanteenById(int canteenId) {

		Cursor cursor = database().rawQuery(
				"SELECT * FROM " + CantinaIplDBContract.CanteenBase.TABLE_NAME
						+ " WHERE "
						+ CantinaIplDBContract.CanteenBase.CANTEEN_ID + " = ?",
				new String[] { String.valueOf(canteenId) });

		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}

	private long insertCanteen(Canteen canteen) throws SQLException {

		for (Meal meal : canteen.getMeals())
			if (MealCanteenRelation.insertMealByCanteenId(meal,
					canteen.getId(), database()) == -1)
				return -1;
			else
				continue;

		ContentValues values = new ContentValues();
		values.put(CantinaIplDBContract.CanteenBase.CANTEEN_ID, canteen.getId());
		values.put(CantinaIplDBContract.CanteenBase.NAME, canteen.getName());
		values.put(CantinaIplDBContract.CanteenBase.ADDRESS,
				canteen.getAddress());
		values.put(CantinaIplDBContract.CanteenBase.AM_PERIOD,
				canteen.getAmPeriod());
		values.put(CantinaIplDBContract.CanteenBase.PM_PERIOD,
				canteen.getPmPeriod());
		values.put(CantinaIplDBContract.CanteenBase.CAMPUS, canteen.getCampus());
		values.put(CantinaIplDBContract.CanteenBase.PHOTO, canteen.getPhoto());
		values.put(CantinaIplDBContract.CanteenBase.LATITUDE,
				canteen.getLatitude());
		values.put(CantinaIplDBContract.CanteenBase.LONGITUDE,
				canteen.getLongitude());
		values.put(CantinaIplDBContract.CanteenBase.ACTIVE, canteen.isActive());
		return database().insert(CantinaIplDBContract.CanteenBase.TABLE_NAME,
				null, values);

	}

	// [ENDREGION] Private_Methods

	// [REGION] Public_Methods

	public LinkedList<Canteen> getCanteens() {

		LinkedList<Canteen> canteens = new LinkedList<Canteen>();
		Cursor cursor = database().query(
				CantinaIplDBContract.CanteenBase.TABLE_NAME,
				new String[] { CantinaIplDBContract.CanteenBase.CANTEEN_ID,
						CantinaIplDBContract.CanteenBase.NAME,
						CantinaIplDBContract.CanteenBase.ADDRESS,
						CantinaIplDBContract.CanteenBase.AM_PERIOD,
						CantinaIplDBContract.CanteenBase.PM_PERIOD,
						CantinaIplDBContract.CanteenBase.CAMPUS,
						CantinaIplDBContract.CanteenBase.PHOTO,
						CantinaIplDBContract.CanteenBase.LATITUDE,
						CantinaIplDBContract.CanteenBase.LONGITUDE,
						CantinaIplDBContract.CanteenBase.ACTIVE }, null, null,
				null, null, CantinaIplDBContract.CanteenBase.NAME);

		if (cursor != null) {
			cursor.moveToFirst();
		}

		if (cursor.moveToFirst()) {
			do {
				canteens.add(new Canteen(Integer.parseInt(cursor.getString(0)),
						cursor.getString(1), cursor.getString(2), cursor
								.getString(3), cursor.getString(4), cursor
								.getString(5), cursor.getString(6), Double
								.parseDouble(cursor.getString(7)), Double
								.parseDouble(cursor.getString(8)), Boolean
								.parseBoolean(cursor.getString(9)), null));
			} while (cursor.moveToNext());
		}
		cursor.close();

		return canteens;
	}

	public void insertCanteens(LinkedList<Canteen> canteens) {

		for (Canteen canteen : canteens) {
			if (canteen != null) {
				Cursor cursor = getCanteenById(canteen.getId());
				if (cursor.getCount() == 0)
					insertCanteen(canteen);
				cursor.close();
			}
		}
	}

	// [ENDREGION] Public_Methods

}
