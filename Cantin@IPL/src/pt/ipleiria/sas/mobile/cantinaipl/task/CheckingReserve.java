package pt.ipleiria.sas.mobile.cantinaipl.task;

import org.json.JSONException;
import org.json.JSONObject;

import pt.ipleiria.sas.mobile.cantinaipl.ReserveDetailsActivity;
import android.content.Context;
import android.util.Log;

public class CheckingReserve extends DataLoading<String, Boolean, Boolean> {

	// [REGION] Constants

	private static final String TAG = "CHECKING_RESERVE";
	private static final String SERVICE_METHOD = getServiceUrl()
			+ "IsReserveValid/" + getServiceAppPassword() + "$";

	// [ENDREGION] Constants

	public CheckingReserve(Context context) {
		super(context);
	}

	@Override
	protected Boolean doInBackground(String... params) {

		for (String param : params) {

			boolean isValid = true;

			while (isValid) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					Log.w(TAG, e.getLocalizedMessage());
				}
				isValid = checkReserve(param);
			}

			return isValid;
		}

		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);

		if (!result)
			((ReserveDetailsActivity) super.getContext()).showDialog(0);
	}

	private boolean checkReserve(String param) {

		boolean isValid = true;

		try {
			JSONObject jsonObject = new JSONObject(
					super.readJsonData(SERVICE_METHOD + param));
			Log.i(TAG, "Checking if is a valid reserve.");

			isValid = jsonObject.getBoolean("Result");

		} catch (JSONException e) {
			e.printStackTrace();
			return true;
		}

		return isValid;
	}

}
