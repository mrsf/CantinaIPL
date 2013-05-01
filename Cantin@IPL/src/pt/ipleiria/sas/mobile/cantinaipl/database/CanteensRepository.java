package pt.ipleiria.sas.mobile.cantinaipl.database;

import java.util.ArrayList;

import pt.ipleiria.sas.mobile.cantinaipl.model.Canteen;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

/**
 * @author Márcio Francisco and Mário Correia
 * @version 2013.0430
 * @since 1.0
 *
 */
public class CanteensRepository extends CantinaIplDBRepository {

	private static final String CREATE_TABLE_CANTEEN = "CREATE TABLE IF NOT EXISTS "
			+ CantinaIplContract.CanteenBase.TABLE_NAME
			+ " ("
			+ CantinaIplContract.CanteenBase.CANTEEN_ID
			+ CantinaIplContract.PRIMARY_KEY_TYPE
			+ CantinaIplContract.COMMA_SEP
			+ CantinaIplContract.CanteenBase.NAME
			+ CantinaIplContract.TEXT_TYPE
			+ CantinaIplContract.COMMA_SEP
			+ CantinaIplContract.CanteenBase.ADDRESS
			+ CantinaIplContract.TEXT_TYPE
			+ CantinaIplContract.COMMA_SEP
			+ CantinaIplContract.CanteenBase.LUNCH_HORARY
			+ CantinaIplContract.TEXT_TYPE
			+ CantinaIplContract.COMMA_SEP
			+ CantinaIplContract.CanteenBase.DINNER_HORARY
			+ CantinaIplContract.TEXT_TYPE
			+ CantinaIplContract.COMMA_SEP
			+ CantinaIplContract.CanteenBase.CAMPUS
			+ CantinaIplContract.TEXT_TYPE
			+ CantinaIplContract.COMMA_SEP
			+ CantinaIplContract.CanteenBase.PHOTO_URL
			+ CantinaIplContract.TEXT_TYPE
			+ CantinaIplContract.COMMA_SEP
			+ CantinaIplContract.CanteenBase.LATITUDE
			+ CantinaIplContract.REAL_TYPE
			+ CantinaIplContract.COMMA_SEP
			+ CantinaIplContract.CanteenBase.LONGITUDE
			+ CantinaIplContract.REAL_TYPE + ")";

	private static final String DELETE_TABLE_CANTEEN = "DROP TABLE IF EXISTS "
			+ CantinaIplContract.CanteenBase.TABLE_NAME;

	public CanteensRepository(Context ctx, Boolean dbUpdate) {
		super(ctx, dbUpdate, CREATE_TABLE_CANTEEN, DELETE_TABLE_CANTEEN);
	}

	public Cursor GetCanteenById(int canteenId) {

		Cursor cursor = database().rawQuery(
				"SELECT * FROM canteen WHERE canteenid = " + canteenId + "",
				null);

		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}

	public ArrayList<Canteen> GetCanteens() {

		ArrayList<Canteen> canteens = new ArrayList<Canteen>();
		Cursor cursor = database().query(
				"canteen",
				new String[] { "canteenid", "name", "address", "lunchhorary",
						"dinnerhorary", "campus", "photourl", "latitude",
						"longitude" }, null, null, null, null, "name");

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
								.parseDouble(cursor.getString(8))));
			} while (cursor.moveToNext());
		}

		return canteens;
	}

	public void InsertCanteens(ArrayList<Canteen> canteens) {

		for (Canteen canteen : canteens) {
			if (canteen != null) {
				if (GetCanteenById(canteen.getCanteen_id()).getCount() == 0)
					InsertCanteen(canteen);
			}
		}
	}

	private long InsertCanteen(Canteen canteen) throws SQLException {
		ContentValues values = new ContentValues();
		values.put("canteenid", canteen.getCanteen_id());
		values.put("name", canteen.getName());
		values.put("address", canteen.getAddress());
		values.put("lunchhorary", canteen.getLunchHorary());
		values.put("dinnerhorary", canteen.getDinnerHorary());
		values.put("campus", canteen.getCampus());
		values.put("photourl", canteen.getPhotoUrl());
		values.put("latitude", canteen.getLatitude());
		values.put("longitude", canteen.getLongitude());
		return database().insert("canteen", null, values);
	}

}
