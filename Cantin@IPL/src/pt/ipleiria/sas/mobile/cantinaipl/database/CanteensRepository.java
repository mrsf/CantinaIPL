package pt.ipleiria.sas.mobile.cantinaipl.database;

import java.util.ArrayList;

import pt.ipleiria.sas.mobile.cantinaipl.R;
import pt.ipleiria.sas.mobile.cantinaipl.model.Canteen;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

public class CanteensRepository extends CantinaIplDBRepository {

	private static final String CREATE_TABLE_CANTEEN = "CREATE TABLE IF NOT EXISTS "
			+ CantinaIplContract.CanteenBase.TABLE_NAME
			+ " ("
			+ CantinaIplContract.CanteenBase.COLUMN_NAME_CANTEEN_ID
			+ CantinaIplContract.PRIMARY_KEY_TYPE
			+ CantinaIplContract.COMMA_SEP
			+ CantinaIplContract.CanteenBase.COLUMN_NAME_NAME
			+ CantinaIplContract.TEXT_TYPE
			+ CantinaIplContract.COMMA_SEP
			+ CantinaIplContract.CanteenBase.COLUMN_NAME_ADDRESS
			+ CantinaIplContract.TEXT_TYPE
			+ CantinaIplContract.COMMA_SEP
			+ CantinaIplContract.CanteenBase.COLUMN_NAME_LUNCHHORARY
			+ CantinaIplContract.TEXT_TYPE
			+ CantinaIplContract.COMMA_SEP
			+ CantinaIplContract.CanteenBase.COLUMN_NAME_DINNERHORARY
			+ CantinaIplContract.TEXT_TYPE
			+ CantinaIplContract.COMMA_SEP
			+ CantinaIplContract.CanteenBase.COLUMN_NAME_CAMPUS
			+ CantinaIplContract.TEXT_TYPE
			+ CantinaIplContract.COMMA_SEP
			+ CantinaIplContract.CanteenBase.COLUMN_NAME_PHOTO
			+ CantinaIplContract.INTEGER_TYPE
			+ CantinaIplContract.COMMA_SEP
			+ CantinaIplContract.CanteenBase.COLUMN_NAME_LATITUDE
			+ CantinaIplContract.REAL_TYPE
			+ CantinaIplContract.COMMA_SEP
			+ CantinaIplContract.CanteenBase.COLUMN_NAME_LONGITUDE
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

	public Cursor GetCanteens() {

		Cursor cursor = database().query(
				"canteen",
				new String[] { "canteenid", "name", "address", "lunchhorary",
						"dinnerhorary", "campus", "photo", "latitude",
						"longitude" }, null, null, null, null, "name");

		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}

	public void populateTable() {

		ArrayList<Canteen> dbCanteens = new ArrayList<Canteen>();

		dbCanteens.add(new Canteen(1, "Cantina1", "ESECS", "13h-14h",
				"20h-21h", "Campus1", R.drawable.cipl_logo, 30.5, 8.2));
		dbCanteens.add(new Canteen(2, "Cantina2", "ESSLEI", "13h-14h",
				"20h-21h", "Campus2", R.drawable.cipl_logo, 30.5, 8.3));
		dbCanteens.add(new Canteen(3, "Cantina3", "ESTG", "13h-14h",
				"encerrada", "Campus2", R.drawable.cipl_logo, 30.5, 8.4));

		InsertCanteens(dbCanteens);

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
		values.put("photo", canteen.getPhoto());
		values.put("latitude", canteen.getLatitude());
		values.put("longitude", canteen.getLongitude());
		return database().insert("canteen", null, values);
	}

}
