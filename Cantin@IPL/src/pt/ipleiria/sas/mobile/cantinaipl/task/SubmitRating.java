package pt.ipleiria.sas.mobile.cantinaipl.task;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class SubmitRating extends DataLoading<String, Boolean, Boolean> {
	
	// [REGION] Constants

	private static final String TAG = "SUBMIT_RATING";
	private static final String SERVICE_METHOD = getServiceUrl()
			+ "SubmitRating/";

	// [ENDREGION] Constants

	public SubmitRating(Context context) {
		super(context);
	}

	@Override
	protected Boolean doInBackground(String... params) {
		boolean isSubmited = false;

		for (String param : params) {
			try {
				JSONObject jsonObject = new JSONObject(
						super.readJsonData(SERVICE_METHOD + param));
				Log.i(TAG, "Submit a rating.");

				isSubmited = jsonObject.getBoolean("Result");

			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
		}

		return isSubmited;
	}

}
