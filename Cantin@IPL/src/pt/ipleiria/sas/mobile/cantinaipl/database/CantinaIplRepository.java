package pt.ipleiria.sas.mobile.cantinaipl.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author Márcio Francisco and Mário Correia
 * @version 2013.0606
 * @since 1.0
 * 
 */
public class CantinaIplRepository {

	// [REGION] Constants
	
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "CantinaIPL.db";

	// [ENDREGION] Constants

	// [REGION] Variables
	
	private CantinaIplDBHelper dbHelper;
	private SQLiteDatabase mDb;
	
	// [ENDREGION] Variables
	
	// [REGION] Final_Variables
		
	private final Context ctx;
	private final Boolean dbUpdate;	
	private final String[] sql_create_entries;
	private final String[] sql_delete_entries;

	// [ENDREGION] Final_Variables

	// [REGION] Constructors
	
	public CantinaIplRepository(Context ctx, Boolean dbUpdate,
			String[] sql_create_entries, String[] sql_delete_entries) {
		this.ctx = ctx;
		this.dbUpdate = dbUpdate;
		this.sql_create_entries = sql_create_entries;
		this.sql_delete_entries = sql_delete_entries;
	}

	// [ENDREGION] Constructors

	// [REGION] Methods
	
	public CantinaIplRepository open() throws SQLException {

		dbHelper = new CantinaIplDBHelper(ctx, DATABASE_NAME, null,
				DATABASE_VERSION, sql_create_entries, sql_delete_entries);

		if (dbUpdate == null)
			mDb = dbHelper.getReadableDatabase();
		else
			mDb = dbHelper.getWritableDatabase();

		return this;
	}

	public void close() {
		dbHelper.close();
	}

	public SQLiteDatabase database() {
		return mDb;
	}

	// [ENDREGION] Methods

}
