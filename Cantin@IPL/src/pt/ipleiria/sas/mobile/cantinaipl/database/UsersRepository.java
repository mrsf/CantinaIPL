package pt.ipleiria.sas.mobile.cantinaipl.database;

import pt.ipleiria.sas.mobile.cantinaipl.model.User;

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
public class UsersRepository extends CantinaIplRepository {

	// [REGION] SQL_Statements

	public static final String[] CREATE_TABLE_USER = new String[] { "CREATE TABLE IF NOT EXISTS "
			+ CantinaIplDBContract.UserBase.TABLE_NAME
			+ " ("
			+ CantinaIplDBContract.UserBase.LOGIN
			+ CantinaIplDBContract.TEXT_TYPE
			+ CantinaIplDBContract.PRIMARY_KEY
			+ CantinaIplDBContract.COMMA_SEP
			+ CantinaIplDBContract.UserBase.BI
			+ CantinaIplDBContract.NUMERIC_TYPE
			+ CantinaIplDBContract.COMMA_SEP
			+ CantinaIplDBContract.UserBase.NAME
			+ CantinaIplDBContract.TEXT_TYPE
			+ CantinaIplDBContract.COMMA_SEP
			+ CantinaIplDBContract.UserBase.COURSE
			+ CantinaIplDBContract.TEXT_TYPE
			+ CantinaIplDBContract.COMMA_SEP
			+ CantinaIplDBContract.UserBase.REGIME
			+ CantinaIplDBContract.NUMERIC_TYPE
			+ CantinaIplDBContract.COMMA_SEP
			+ CantinaIplDBContract.UserBase.PHOTO
			+ CantinaIplDBContract.TEXT_TYPE
			+ CantinaIplDBContract.COMMA_SEP
			+ CantinaIplDBContract.UserBase.NIF
			+ CantinaIplDBContract.NUMERIC_TYPE
			+ CantinaIplDBContract.COMMA_SEP
			+ CantinaIplDBContract.UserBase.EMAIL
			+ CantinaIplDBContract.TEXT_TYPE
			+ CantinaIplDBContract.COMMA_SEP
			+ CantinaIplDBContract.UserBase.TYPE
			+ CantinaIplDBContract.NUMERIC_TYPE
			+ CantinaIplDBContract.COMMA_SEP
			+ CantinaIplDBContract.UserBase.ACTIVE
			+ CantinaIplDBContract.NUMERIC_TYPE
			+ CantinaIplDBContract.COMMA_SEP
			+ CantinaIplDBContract.UserBase.SCHOOL
			+ CantinaIplDBContract.TEXT_TYPE + ")" };

	public static final String[] DELETE_TABLE_USER = new String[] { "DROP TABLE IF EXISTS "
			+ CantinaIplDBContract.UserBase.TABLE_NAME };

	// [ENDREGION] SQL_Statements

	// [REGION] Constructors

	public UsersRepository(Context ctx, Boolean dbUpdate) {
		super(ctx, dbUpdate, CREATE_TABLE_USER, DELETE_TABLE_USER);
	}

	// [ENDREGION] Constructors

	// [REGION] Methods

	public Cursor getUserByLogin(String login) {

		Cursor cursor = database().rawQuery(
				"SELECT * FROM " + CantinaIplDBContract.UserBase.TABLE_NAME
						+ " WHERE " + CantinaIplDBContract.UserBase.LOGIN
						+ " = ?", new String[] { login });

		if (cursor != null) {
			cursor.moveToFirst();
		}

		return cursor;
	}

	public long insertUser(User user) throws SQLException {
		if (user != null) {
			Cursor cursor = getUserByLogin(user.getUserName());
			if (cursor.getCount() == 0) {
				ContentValues values = new ContentValues();
				values.put(CantinaIplDBContract.UserBase.LOGIN, user.getUserName());
				values.put(CantinaIplDBContract.UserBase.BI, user.getBi());
				values.put(CantinaIplDBContract.UserBase.NAME, user.getName());
				values.put(CantinaIplDBContract.UserBase.COURSE,
						user.getCourse());
				values.put(CantinaIplDBContract.UserBase.REGIME,
						user.getRegime());
				values.put(CantinaIplDBContract.UserBase.PHOTO, user.getPhoto());
				values.put(CantinaIplDBContract.UserBase.NIF, user.getNif());
				values.put(CantinaIplDBContract.UserBase.EMAIL, user.getEmail());
				values.put(CantinaIplDBContract.UserBase.TYPE, user.getType());
				values.put(CantinaIplDBContract.UserBase.ACTIVE,
						user.isActive());
				values.put(CantinaIplDBContract.UserBase.SCHOOL,
						user.getSchool());
				cursor.close();
				return database().insert(
						CantinaIplDBContract.UserBase.TABLE_NAME, null, values);
			}
			cursor.close();
		}
		return 0;
	}

	// [ENDREGION] Methods

}
