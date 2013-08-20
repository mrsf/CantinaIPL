package pt.ipleiria.sas.mobile.cantinaipl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import com.actionbarsherlock.view.MenuInflater;

import pt.ipleiria.sas.mobile.cantinaipl.controller.UserSingleton;
import pt.ipleiria.sas.mobile.cantinaipl.database.UsersRepository;
import pt.ipleiria.sas.mobile.cantinaipl.model.User;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

public class UserActivity extends ClosableActivity {

	private User user;

	private EditText nameEdit;
	private String nameValue;
	private EditText courseEdit;
	private String courseValue;
	private EditText numberEdit;
	private String numberValue;
	private EditText schoolEdit;
	private String schoolValue;
	private EditText biEdit;
	private String biValue;
	private EditText nifEdit;
	private int nifValue;
	private RadioGroup regimeRadioG;
	private boolean regimeValue;
	private Button confirmationButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);

		this.user = UserSingleton.getInstance().getUser();

		this.nameEdit = (EditText) findViewById(R.id.et_user_name);
		this.nameEdit.setText(this.nameEdit.getHint() + ": " + user.getName());
		this.nameEdit.setEnabled(false);

		this.courseEdit = (EditText) findViewById(R.id.et_user_course);
		this.courseEdit.setText(this.courseEdit.getHint() + ": "
				+ user.getCourse());
		this.courseEdit.setEnabled(false);

		this.numberEdit = (EditText) findViewById(R.id.et_user_number);
		this.numberEdit.setText(this.numberEdit.getHint() + ": "
				+ user.getLogin());
		this.numberEdit.setEnabled(false);

		this.schoolEdit = (EditText) findViewById(R.id.et_user_school);
		this.schoolEdit.setText(this.schoolEdit.getHint() + ": "
				+ user.getSchool());
		this.schoolEdit.setEnabled(false);

		this.biEdit = (EditText) findViewById(R.id.et_user_bi);
		this.nifEdit = (EditText) findViewById(R.id.et_user_nif);

		this.regimeRadioG = (RadioGroup) findViewById(R.id.rg_user_regime);
		this.regimeRadioG.setOnCheckedChangeListener(this.regimeRadioGListener);

		this.confirmationButton = (Button) findViewById(R.id.bt_user_confirmation);
		this.confirmationButton.setOnClickListener(this.confirmationAction);
	}

	OnCheckedChangeListener regimeRadioGListener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.radio0:
				regimeValue = false;
				UserSingleton.getInstance().getUser().setRegime(regimeValue);
				break;
			case R.id.radio1:
				regimeValue = true;
				UserSingleton.getInstance().getUser().setRegime(regimeValue);
				break;
			}
		}
	};

	OnClickListener confirmationAction = new OnClickListener() {
		@Override
		public void onClick(View v) {

			nameValue = UserSingleton.getInstance().getUser().getName();
			courseValue = UserSingleton.getInstance().getUser().getCourse();
			numberValue = UserSingleton.getInstance().getUser().getLogin();
			schoolValue = UserSingleton.getInstance().getUser().getSchool();
			biValue = biEdit.getText().toString();
			nifValue = Integer.parseInt(nifEdit.getText().toString());

			new InsertUserTask().executeOnExecutor(getExec(), (Void) null);
		}
	};

	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.user, menu);
		return true;
	}

	public void attemptStoreUser() {

		/*
		 * // Reset errors. mLoginView.setError(null);
		 * mPasswordView.setError(null);
		 * 
		 * // Store values at the time of the login attempt. mLogin =
		 * mLoginView.getText().toString(); mPassword =
		 * mPasswordView.getText().toString();
		 */

	}

	// [REGION] Thread

	/**
	 * Represents an asynchronous task used to insert data of the user.
	 */
	public class InsertUserTask extends AsyncTask<Void, Void, Boolean> {

		private static final String TAG = "INSERT_USER";
		private static final String APP_PASSWORD = "ipl.cantina.1213@gmail.com";
		private static final String SERVICE_URL = "http://25.42.150.3/CantinaIplService.svc/InsertUser/";

		private UsersRepository usersRepository;

		@Override
		protected Boolean doInBackground(Void... params) {

			try {

				String url = SERVICE_URL
						+ numberValue
						+ "$"
						+ APP_PASSWORD
						+ "$"
						+ URLEncoder.encode(courseValue, "UTF-8")
						+ "$"
						+ UserSingleton.getInstance().getUser().getEmail()
						+ "$"
						+ biValue
						+ "$"
						+ URLEncoder.encode(nameValue, "UTF-8")
						+ "$"
						+ String.valueOf(nifValue)
						+ "$"
						+ String.valueOf(regimeValue)
						+ "$"
						+ URLEncoder.encode(schoolValue, "UTF-8")
						+ "$"
						+ String.valueOf(UserSingleton.getInstance().getUser()
								.isType());
				String data = readJsonData(url.replace("+", "%20"));

				JSONObject jsonObject = new JSONObject(data);
				if (jsonObject != null)
					return jsonObject.getBoolean("Result");

			} catch (Exception e) {
				Log.w(TAG, e.getLocalizedMessage());
			}
			return false;
		}

		@Override
		protected void onPostExecute(final Boolean isInserted) {
			// mAuthTask = null;
			// showProgress(false);

			if (isInserted) {

				usersRepository = new UsersRepository(getApplicationContext(),
						false);
				usersRepository.open();
				usersRepository.insertUser(new User(user.getLogin(), biValue,
						user.getName(), user.getCourse(), user.isRegime(), "",
						nifValue, user.getEmail(), user.isType(), user
								.isActive(), user.getSchool()));
				usersRepository.close();

				UserSingleton.getInstance().getUser().setBi(biValue);
				UserSingleton.getInstance().getUser().setNif(nifValue);

				Toast.makeText(getApplicationContext(),
						"O utilizador foi comfirmado.", Toast.LENGTH_LONG)
						.show();

				Intent intent = new Intent(getApplicationContext(),
						CanteensActivity.class);
				startActivity(intent);

				finish();
			} else { /*
					 * mPasswordView
					 * .setError(getString(R.string.error_incorrect_password));
					 * mPasswordView.requestFocus();
					 */
				Toast.makeText(getApplicationContext(),
						"O utilizador não foi comfirmado.", Toast.LENGTH_LONG)
						.show();
			}

		}

		@Override
		protected void onCancelled() {
			// mAuthTask = null;
			// showProgress(false);
		}

		private String readJsonData(String serviceUrl) {
			StringBuilder stringBuilder = new StringBuilder();
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(serviceUrl);

			try {
				HttpResponse response = httpClient.execute(httpGet);
				StatusLine statusLine = response.getStatusLine();
				int statusCode = statusLine.getStatusCode();
				if (statusCode == 200) {
					HttpEntity httpEntity = response.getEntity();
					InputStream content = httpEntity.getContent();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(content));
					String line;
					while ((line = reader.readLine()) != null) {
						stringBuilder.append(line);
					}
				} else {
					Log.e(TAG, "Failed to get Json data file.");
				}
			} catch (ClientProtocolException e) {
				Log.w(TAG, e.getLocalizedMessage());
			} catch (IOException e) {
				Log.w(TAG, e.getLocalizedMessage());
			}

			return stringBuilder.toString();
		}

	}

	// [ENDREGION] Thread
}
