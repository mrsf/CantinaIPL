package pt.ipleiria.sas.mobile.cantinaipl.task;

import org.json.JSONException;
import org.json.JSONObject;

import pt.ipleiria.sas.mobile.cantinaipl.controller.ReserveListAdapter;
import pt.ipleiria.sas.mobile.cantinaipl.database.ReservesRepository;
import pt.ipleiria.sas.mobile.cantinaipl.model.Reserve;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * @author Márcio Francisco and Mário Correia
 * @version 2013.0701
 * @since 1.0
 * 
 */
public class CancellingReserve extends DataLoading<String, Boolean, Boolean> {

	// [REGION] Constants

	private static final String TAG = "CANCELLING_RESERVE";
	private static final String SERVICE_METHOD = getServiceUrl()
			+ "CancelReserve/";

	// [ENDREGION] Constants

	// [REGION] Variables

	private ProgressDialog progressDialog;
	private ReserveListAdapter reserveListAdapter;
	private Reserve itemToRemove;

	// [ENDREGION] Variables

	// [REGION] Constructors

	public CancellingReserve(Context context, ProgressDialog progressDialog,
			ReserveListAdapter reserveListAdapter, Reserve itemToRemove) {
		super(context);
		this.progressDialog = progressDialog;
		this.reserveListAdapter = reserveListAdapter;
		this.itemToRemove = itemToRemove;
	}

	// [ENDREGION] Constructors

	// [REGION] Inherited_Methods

	@Override
	protected Boolean doInBackground(String... params) {

		boolean isMake = false;

		for (String param : params) {
			try {
				JSONObject jsonObject = new JSONObject(
						super.readJsonData(SERVICE_METHOD + param));
				Log.i(TAG, "Cancelling a reserve.");

				isMake = jsonObject.getBoolean("Result");

			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
		}

		return isMake;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);

		if (result) {
			ReservesRepository reservesRepository = new ReservesRepository(
					super.getContext(), false);
			reservesRepository.open();
			reservesRepository.close();

			this.progressDialog.dismiss();
			this.reserveListAdapter.remove(itemToRemove);
			Toast.makeText(super.getContext(), "Reserva anulada com sucesso.",
					Toast.LENGTH_SHORT).show();
		} else {
			this.progressDialog.dismiss();
			Toast.makeText(super.getContext(),
					"Não é possível anular a reserva!", Toast.LENGTH_SHORT)
					.show();
		}
	}

	// [ENDREGION] Inherited_Methods

}
