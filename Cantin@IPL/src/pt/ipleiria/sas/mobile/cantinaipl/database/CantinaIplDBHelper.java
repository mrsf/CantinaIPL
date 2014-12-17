package pt.ipleiria.sas.mobile.cantinaipl.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author Márcio Francisco and Mário Correia
 * @version 2013.0606
 * @since 1.0
 * 
 */
public class CantinaIplDBHelper extends SQLiteOpenHelper {

	// [REGION] Constants
	
	private static final String TAG = "CANTINAIPL_DBHELPER";

	// [ENDREGION] Constants

	// [REGION] SQL_Statements_Arrays

	private final String[] sql_create_entries;
	private final String[] sql_delete_entries;

	// [ENDREGION] SQL_Statements_Arrays

	// [REGION] Constructors

	public CantinaIplDBHelper(Context context, String name,
			CursorFactory factory, int version, String[] sql_create_entries,
			String[] sql_delete_entries) {
		super(context, name, factory, version);
		this.sql_create_entries = sql_create_entries;
		this.sql_delete_entries = sql_delete_entries;
	}

	// [ENDREGION] Constructors

	// [REGION] Inherited_Methods

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.w(TAG, "A criar a base de dados ...");
		for (String s : sql_create_entries)
			db.execSQL(s);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(TAG, "A actualizar da versão " + oldVersion + " para a versão "
				+ newVersion + "...");
		for (String s : sql_delete_entries)
			db.execSQL(s);

		this.onCreate(db);
	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}

	// [ENDREGION] Inherited_Methods

}
