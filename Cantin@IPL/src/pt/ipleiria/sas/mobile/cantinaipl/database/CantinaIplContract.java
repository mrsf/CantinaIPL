package pt.ipleiria.sas.mobile.cantinaipl.database;

import android.provider.BaseColumns;

public abstract class CantinaIplContract {

	// [REGION] Table Data Types Statements

	public static final String PRIMARY_KEY_TYPE = " INTEGER PRIMARY KEY";
	public static final String TEXT_TYPE = " TEXT";
	public static final String INTEGER_TYPE = " INTEGER";
	public static final String REAL_TYPE = " REAL";
	public static final String COMMA_SEP = ", ";

	// [ENDREGION] Table Data Types Statements

	// [REGION] Table Base Columns Names

	public static abstract class UserBase implements BaseColumns {
		public static final String TABLE_NAME = "user";
		public static final String COLUMN_NAME_USER_ID = "userid";
		public static final String COLUMN_NAME_USERNAME = "username";
		public static final String COLUMN_NAME_PASSWORD = "password";
		public static final String COLUMN_NAME_GROUP = "group";
	}

	public static abstract class CanteenBase implements BaseColumns {
		public static final String TABLE_NAME = "canteen";
		public static final String COLUMN_NAME_CANTEEN_ID = "canteenid";
		public static final String COLUMN_NAME_NAME = "name";
		public static final String COLUMN_NAME_ADDRESS = "address";
		public static final String COLUMN_NAME_LUNCHHORARY = "lunchhorary";
		public static final String COLUMN_NAME_DINNERHORARY = "dinnerhorary";
		public static final String COLUMN_NAME_CAMPUS = "campus";
		public static final String COLUMN_NAME_PHOTO = "photo";
		public static final String COLUMN_NAME_LATITUDE = "latitude";
		public static final String COLUMN_NAME_LONGITUDE = "longitude";
	}

	// [ENDREGION] Table Base Columns Names

	// Prevents the CantinaIPLContract class from being instantiated.
	// [REGION] Constructor

	private CantinaIplContract() {
	}

	// [ENDREGION] Constructor

}
