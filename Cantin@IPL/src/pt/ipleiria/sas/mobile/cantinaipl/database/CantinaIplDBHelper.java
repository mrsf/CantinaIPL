package pt.ipleiria.sas.mobile.cantinaipl.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CantinaIplDBHelper extends SQLiteOpenHelper {

	private static final String TAG = "CantinaIPL";

	private String sql_create_entries;
	private String sql_delete_entries;

	public CantinaIplDBHelper(Context context, String name,
			CursorFactory factory, int version, String sql_create_entries,
			String sql_delete_entries) {
		super(context, name, factory, version);		
		this.sql_create_entries = sql_create_entries;
		this.sql_delete_entries = sql_delete_entries;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.w(TAG, "A criar a base de dados ...");
		db.execSQL(sql_create_entries);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(TAG, "A actualizar da versão " + oldVersion + " para a versão "
				+ newVersion + "...");
		db.execSQL(sql_delete_entries);
		this.onCreate(db);
	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}

}
