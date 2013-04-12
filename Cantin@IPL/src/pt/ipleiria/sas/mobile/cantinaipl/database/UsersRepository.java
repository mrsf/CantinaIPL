package pt.ipleiria.sas.mobile.cantinaipl.database;

import java.util.ArrayList;

import pt.ipleiria.sas.mobile.cantinaipl.model.User;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

public class UsersRepository extends CantinaIplDBRepository {

	private static final String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS "
			+ CantinaIplContract.UserBase.TABLE_NAME
			+ " ("
			+ CantinaIplContract.UserBase.COLUMN_NAME_USER_ID
			+ CantinaIplContract.PRIMARY_KEY_TYPE
			+ CantinaIplContract.COMMA_SEP
			+ CantinaIplContract.UserBase.COLUMN_NAME_USERNAME
			+ CantinaIplContract.TEXT_TYPE
			+ CantinaIplContract.COMMA_SEP
			+ CantinaIplContract.UserBase.COLUMN_NAME_PASSWORD
			+ CantinaIplContract.TEXT_TYPE
			+ CantinaIplContract.COMMA_SEP
			+ CantinaIplContract.UserBase.COLUMN_NAME_GROUP
			+ CantinaIplContract.TEXT_TYPE + ")";

	private static final String DELETE_TABLE_USER = "DROP TABLE IF EXISTS "
			+ CantinaIplContract.UserBase.TABLE_NAME;

	public UsersRepository(Context ctx, Boolean dbUpdate) {
		super(ctx, dbUpdate, CREATE_TABLE_USER, DELETE_TABLE_USER);
	}

	public Cursor GetUserById(int userId) {

		Cursor cursor = database().rawQuery(
				"SELECT * FROM user WHERE userid = " + userId + "", null);

		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}

	public void populateTable() {

		ArrayList<User> usersList = new ArrayList<User>();

		usersList.add(new User(1, "2091112", "2091112", "Estudantes"));
		usersList.add(new User(2, "marcio.francisco", "marciofrancisco",
				"Funcionarios"));

		InsertUsers(usersList);

	}

	public void InsertUsers(ArrayList<User> users) {

		for (User user : users) {
			if (user != null) {
				if (GetUserById(user.getUser_id()).getCount() == 0)
					InsertUser(user);
			}
		}
	}

	private long InsertUser(User user) throws SQLException {
		ContentValues values = new ContentValues();
		values.put("userid", user.getUser_id());
		values.put("username", user.getUsername());
		values.put("password", user.getPassword());
		values.put("group", user.getGroup());
		return database().insert("user", null, values);
	}

}
