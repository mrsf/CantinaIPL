package pt.ipleiria.sas.mobile.cantinaipl.task;

import org.json.JSONException;
import org.json.JSONObject;

import pt.ipleiria.sas.mobile.cantinaipl.BaseActivity;
import pt.ipleiria.sas.mobile.cantinaipl.controller.AccountSingleton;
import pt.ipleiria.sas.mobile.cantinaipl.model.Account;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

public class AccountLoading extends DataLoading<String, Account, Void> {

	// [REGION] Constants

	private static final String TAG = "ACCOUNT_LOADING";
	private static final String SERVICE_METHOD = getServiceUrl()
			+ "GetAccount/";

	// [ENDREGION] Constants

	private TextView balance;

	public AccountLoading(Context context, TextView balance) {
		super(context);
		this.balance = balance;
	}

	@Override
	protected Void doInBackground(String... params) {

		for (String param : params) {
			try {
				JSONObject jsonObject = new JSONObject(
						super.readJsonData(SERVICE_METHOD + param));
				Log.i(TAG, "Loading account data.");

				AccountSingleton.getInstance().account = new Account(
						jsonObject.getInt("Id"),
						jsonObject.getDouble("Balance"),
						jsonObject.getString("UserLogin"));
				publishProgress(AccountSingleton.getInstance().getAccount());
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
		}

		return null;
	}

	@Override
	protected void onProgressUpdate(Account... values) {
		super.onProgressUpdate(values);

		for (Account value : values) {
			this.balance.setText(((BaseActivity) super.getContext())
					.formatStringToDecimal(String.valueOf(value.getBalance())));
		}

	}

}
