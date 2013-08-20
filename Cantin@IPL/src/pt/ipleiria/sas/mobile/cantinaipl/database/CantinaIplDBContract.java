package pt.ipleiria.sas.mobile.cantinaipl.database;

import android.provider.BaseColumns;

/**
 * @author Márcio Francisco and Mário Correia
 * @version 2013.0606
 * @since 1.0
 * 
 */
abstract class CantinaIplDBContract {

	// [REGION] Table Data Types Statements

	public static final String PRIMARY_KEY = " PRIMARY KEY";
	public static final String TEXT_TYPE = " TEXT";
	public static final String INTEGER_TYPE = " INTEGER";
	public static final String REAL_TYPE = " REAL";
	public static final String NUMERIC_TYPE = " NUMERIC";
	public static final String COMMA_SEP = ", ";

	// [ENDREGION] Table Data Types Statements

	// [REGION] Table Base Columns Names

	public static abstract class UserBase implements BaseColumns {
		public static final String TABLE_NAME = "user";
		public static final String LOGIN = "login";
		public static final String BI = "bi";
		public static final String NAME = "name";
		public static final String COURSE = "course";
		public static final String REGIME = "regime";
		public static final String PHOTO = "photo";
		public static final String NIF = "nif";
		public static final String EMAIL = "email";
		public static final String TYPE = "type";
		public static final String ACTIVE = "active";
		public static final String SCHOOL = "school";
	}

	public static abstract class CanteenBase implements BaseColumns {
		public static final String TABLE_NAME = "canteen";
		public static final String CANTEEN_ID = "canteenid";
		public static final String NAME = "name";
		public static final String ADDRESS = "address";
		public static final String AM_PERIOD = "amperiod";
		public static final String PM_PERIOD = "pmperiod";
		public static final String CAMPUS = "campus";
		public static final String PHOTO = "photo";
		public static final String LATITUDE = "latitude";
		public static final String LONGITUDE = "longitude";
		public static final String ACTIVE = "active";
	}

	public static abstract class MealCanteenBase implements BaseColumns {
		public static final String TABLE_NAME = "mealcanteen";
		public static final String CANTEEN_ID = "canteenid";
		public static final String MEAL_ID = "mealid";
	}

	public static abstract class MealBase implements BaseColumns {
		public static final String TABLE_NAME = "meal";
		public static final String MEAL_ID = "mealid";
		public static final String DATE = "date";
		public static final String REFEICAO = "refeicao";
		public static final String TYPE = "type";
		public static final String PRICE = "price";
	}

	public static abstract class MealDishBase implements BaseColumns {
		public static final String TABLE_NAME = "mealdish";
		public static final String DISH_ID = "dishid";
		public static final String MEAL_ID = "mealid";
	}

	public static abstract class DishBase implements BaseColumns {
		public static final String TABLE_NAME = "dish";
		public static final String DISH_ID = "dishid";
		public static final String PHOTO = "photo";
		public static final String DESCRIPTION = "description";
		public static final String NAME = "name";
		public static final String PRICE = "price";
		public static final String TYPE = "type";
		public static final String RATING = "rating";
	}

	public static abstract class ReferenceBase implements BaseColumns {
		public static final String TABLE_NAME = "reference";
		public static final String REF_ID = "refid";
		public static final String ENTITY = "entity";
		public static final String REFERENCE = "reference";
		public static final String AMOUNT = "amount";
		public static final String ACCOUNT_ID = "accountid";
		public static final String EMITION_DATE = "emitiondate";
		public static final String EXPIRATION_DATE = "expirationdate";
		public static final String STATUS = "status";
		public static final String ISPAID = "ispaid";
	}

	public static abstract class ReserveBase implements BaseColumns {
		public static final String TABLE_NAME = "reserve";
		public static final String RES_ID = "resid";
		public static final String PURCHASE_DATE = "purchasedate";
		public static final String USE_DATE = "usedate";
		public static final String PRICE = "price";
		public static final String ISVALID = "isvalid";
		public static final String USER_LOGIN = "userlogin";
		public static final String MEAL_ID = "mealid";
		public static final String ISACCOUNTED = "isaccounted";
	}

	public static abstract class ReserveDishBase implements BaseColumns {
		public static final String TABLE_NAME = "reservedish";
		public static final String RESERVE_ID = "reserveid";
		public static final String DISH_ID = "dishid";
	}

	// [ENDREGION] Table Base Columns Names

	// Prevents the CantinaIPLContract class from being instantiated.
	// [REGION] Constructor

	private CantinaIplDBContract() {
	}

	// [ENDREGION] Constructor

}
