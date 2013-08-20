package pt.ipleiria.sas.mobile.cantinaipl.database;

import pt.ipleiria.sas.mobile.cantinaipl.model.Reference;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

public class ReferencesRepository extends CantinaIplRepository {

	// [REGION] SQL_Statements

	public static final String[] CREATE_TABLE_REFERENCE = new String[] { "CREATE TABLE IF NOT EXISTS "
			+ CantinaIplDBContract.ReferenceBase.TABLE_NAME
			+ " ("
			+ CantinaIplDBContract.ReferenceBase.REF_ID
			+ CantinaIplDBContract.INTEGER_TYPE
			+ CantinaIplDBContract.PRIMARY_KEY
			+ CantinaIplDBContract.COMMA_SEP
			+ CantinaIplDBContract.ReferenceBase.ENTITY
			+ CantinaIplDBContract.TEXT_TYPE
			+ CantinaIplDBContract.COMMA_SEP
			+ CantinaIplDBContract.ReferenceBase.REFERENCE
			+ CantinaIplDBContract.TEXT_TYPE
			+ CantinaIplDBContract.COMMA_SEP
			+ CantinaIplDBContract.ReferenceBase.AMOUNT
			+ CantinaIplDBContract.REAL_TYPE
			+ CantinaIplDBContract.COMMA_SEP
			+ CantinaIplDBContract.ReferenceBase.ACCOUNT_ID
			+ CantinaIplDBContract.INTEGER_TYPE
			+ CantinaIplDBContract.COMMA_SEP
			+ CantinaIplDBContract.ReferenceBase.EMITION_DATE
			+ CantinaIplDBContract.TEXT_TYPE
			+ CantinaIplDBContract.COMMA_SEP
			+ CantinaIplDBContract.ReferenceBase.EXPIRATION_DATE
			+ CantinaIplDBContract.TEXT_TYPE
			+ CantinaIplDBContract.COMMA_SEP
			+ CantinaIplDBContract.ReferenceBase.STATUS
			+ CantinaIplDBContract.TEXT_TYPE
			+ CantinaIplDBContract.COMMA_SEP
			+ CantinaIplDBContract.ReferenceBase.ISPAID
			+ CantinaIplDBContract.NUMERIC_TYPE + ")" };

	public static final String[] DELETE_TABLE_REFERENCE = new String[] { "DROP TABLE IF EXISTS "
			+ CantinaIplDBContract.ReferenceBase.TABLE_NAME };

	// [ENDREGION] SQL_Statements

	// [REGION] Constructors

	public ReferencesRepository(Context ctx, Boolean dbUpdate) {
		super(ctx, dbUpdate, CREATE_TABLE_REFERENCE, DELETE_TABLE_REFERENCE);
	}

	// [ENDREGION] Constructors

	// [REGION] Private_Methods

	private Cursor getReferenceById(int referenceId) {

		Cursor cursor = database().rawQuery(
				"SELECT * FROM "
						+ CantinaIplDBContract.ReferenceBase.TABLE_NAME
						+ " WHERE " + CantinaIplDBContract.ReferenceBase.REF_ID
						+ " = " + referenceId, null);

		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}

	// [ENDREGION] Private_Methods

	// [REGION] Public_Methods

	public long insertReference(Reference reference) throws SQLException {

		if (reference != null) {
			Cursor references = getReferenceById(reference.getId());
			if (references.getCount() == 0) {
				ContentValues values = new ContentValues();
				values.put(CantinaIplDBContract.ReferenceBase.REF_ID,
						reference.getId());
				values.put(CantinaIplDBContract.ReferenceBase.ENTITY,
						reference.getEntity());
				values.put(CantinaIplDBContract.ReferenceBase.REFERENCE,
						reference.getReference());
				values.put(CantinaIplDBContract.ReferenceBase.AMOUNT,
						reference.getAmount());
				values.put(CantinaIplDBContract.ReferenceBase.ACCOUNT_ID,
						reference.getAccountId());
				values.put(CantinaIplDBContract.ReferenceBase.EMITION_DATE,
						reference.getEmitionDate());
				values.put(CantinaIplDBContract.ReferenceBase.EXPIRATION_DATE,
						reference.getExpirationDate());
				values.put(CantinaIplDBContract.ReferenceBase.STATUS,
						reference.getStatus());
				values.put(CantinaIplDBContract.ReferenceBase.ISPAID,
						reference.isPaid());
				references.close();
				return database().insert(
						CantinaIplDBContract.ReferenceBase.TABLE_NAME, null,
						values);
			}
			references.close();
		}
		return -1;

	}

	// [ENDREGION] Public_Methods

}
