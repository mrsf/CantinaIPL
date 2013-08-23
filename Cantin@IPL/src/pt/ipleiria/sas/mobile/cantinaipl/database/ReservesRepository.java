package pt.ipleiria.sas.mobile.cantinaipl.database;

import pt.ipleiria.sas.mobile.cantinaipl.model.Reserve;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

/**
 * <b>Reserves data access class.</b>
 * 
 * <p>
 * This class allows reserves data management in the application database.
 * </p>
 * 
 * @author Márcio Francisco and Mário Correia
 * @version 2013.0612
 * @since 1.0
 * 
 */
public class ReservesRepository extends CantinaIplRepository {

	// [REGION] SQL_Statements

	/**
	 * Constant with SQL statements, that allows reserves data table creation.
	 */
	public static final String[] CREATE_TABLE_RESERVE = new String[] {
			"CREATE TABLE IF NOT EXISTS "
					+ CantinaIplDBContract.ReserveBase.TABLE_NAME + " ("
					+ CantinaIplDBContract.ReserveBase.RES_ID
					+ CantinaIplDBContract.INTEGER_TYPE
					+ CantinaIplDBContract.PRIMARY_KEY
					+ CantinaIplDBContract.COMMA_SEP
					+ CantinaIplDBContract.ReserveBase.PURCHASE_DATE
					+ CantinaIplDBContract.TEXT_TYPE
					+ CantinaIplDBContract.COMMA_SEP
					+ CantinaIplDBContract.ReserveBase.USE_DATE
					+ CantinaIplDBContract.TEXT_TYPE
					+ CantinaIplDBContract.COMMA_SEP
					+ CantinaIplDBContract.ReserveBase.PRICE
					+ CantinaIplDBContract.REAL_TYPE
					+ CantinaIplDBContract.COMMA_SEP
					+ CantinaIplDBContract.ReserveBase.ISVALID
					+ CantinaIplDBContract.NUMERIC_TYPE
					+ CantinaIplDBContract.COMMA_SEP
					+ CantinaIplDBContract.ReserveBase.USER_LOGIN
					+ CantinaIplDBContract.TEXT_TYPE
					+ CantinaIplDBContract.COMMA_SEP
					+ CantinaIplDBContract.ReserveBase.MEAL_ID
					+ CantinaIplDBContract.INTEGER_TYPE
					+ CantinaIplDBContract.COMMA_SEP
					+ CantinaIplDBContract.ReserveBase.ISACCOUNTED
					+ CantinaIplDBContract.NUMERIC_TYPE + ")",
			ReserveDishRelation.CREATE_TABLE_RESERVEDISH };

	/**
	 * Constant with SQL statements, that allows reserves data table deletion.
	 */
	public static final String[] DELETE_TABLE_RESERVE = new String[] {
			"DROP TABLE IF EXISTS "
					+ CantinaIplDBContract.ReserveBase.TABLE_NAME,
			ReserveDishRelation.DELETE_TABLE_RESERVEDISH };

	// [ENDREGION] SQL_Statements

	// [REGION] Constructors

	/**
	 * <b>Constructor to instantiate the class.</b>
	 * 
	 * <p>
	 * This constructor allow instantiate the ReservesRepository class.
	 * </p>
	 * 
	 * @param ctx
	 *            Actual application context.
	 * @param dbUpdate
	 *            Indicate if is a database update.
	 */
	public ReservesRepository(Context ctx, Boolean dbUpdate) {
		super(ctx, dbUpdate, CREATE_TABLE_RESERVE, DELETE_TABLE_RESERVE);
	}

	// [ENDREGION] Constructors

	// [REGION] Private_Methods

	/**
	 * <b>Reserve search method.</b>
	 * 
	 * <p>
	 * This private method, allows knowing if a reserve record is stored in
	 * application database.
	 * </p>
	 * 
	 * @param reserveId
	 *            Reserve database record id.
	 * @return true If reserve record exist in database;<br>
	 *         false If reserve record not exist.
	 */
	private boolean reserveExist(int reserveId) {

		boolean exist = false;
		Cursor cursor = database().rawQuery(
				"SELECT * FROM " + CantinaIplDBContract.ReserveBase.TABLE_NAME
						+ " WHERE " + CantinaIplDBContract.ReserveBase.RES_ID
						+ " = ?", new String[] { String.valueOf(reserveId) });

		if (cursor != null)
			cursor.moveToFirst();

		exist = (cursor.getCount() == 0 ? false : true);
		cursor.close();

		return exist;
	}

	// [ENDREGION] Private_Methods

	// [REGION] Public_Methods

	/**
	 * <b>Method to insert a reserve.</b>
	 * 
	 * <p>
	 * This method allows a reserve record addition on application database. If
	 * occur a SQL exception, its is activated.
	 * </p>
	 * 
	 * @param reserve
	 *            Object with reserve data.
	 * @return The row ID of the newly inserted row;<br>
	 *         -1 If an error occurred.
	 * @throws SQLException
	 *             If a database SQL error occur.
	 */
	public long insertReserve(Reserve reserve) throws SQLException {

		if (reserve != null)
			if (!this.reserveExist(reserve.getId()))
				if (ReserveDishRelation.insertReserveDishes(reserve.getId(),
						reserve.getDishes(), database()) != -1) {

					ContentValues values = new ContentValues();
					values.put(CantinaIplDBContract.ReserveBase.RES_ID,
							reserve.getId());
					values.put(CantinaIplDBContract.ReserveBase.PURCHASE_DATE,
							reserve.getPurchaseDate());
					values.put(CantinaIplDBContract.ReserveBase.USE_DATE,
							reserve.getUseDate());
					values.put(CantinaIplDBContract.ReserveBase.PRICE,
							reserve.getPrice());
					values.put(CantinaIplDBContract.ReserveBase.ISVALID,
							reserve.isValid());
					values.put(CantinaIplDBContract.ReserveBase.USER_LOGIN,
							reserve.getUserName());
					values.put(CantinaIplDBContract.ReserveBase.MEAL_ID,
							reserve.getMealId());
					values.put(CantinaIplDBContract.ReserveBase.ISACCOUNTED,
							reserve.isAccounted());

					return database().insert(
							CantinaIplDBContract.ReserveBase.TABLE_NAME, null,
							values);
				}

		return -1;
	}

	// [ENDREGION] Public_Methods

}
