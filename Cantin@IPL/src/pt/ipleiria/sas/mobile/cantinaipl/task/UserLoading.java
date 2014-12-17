package pt.ipleiria.sas.mobile.cantinaipl.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pt.ipleiria.sas.mobile.cantinaipl.controller.UserSingleton;
import pt.ipleiria.sas.mobile.cantinaipl.model.User;

import android.os.AsyncTask;
import android.util.Log;

public class UserLoading extends AsyncTask<String, User, User> {

	// [REGION] Constants

	private static final String TAG = "USER_LOADING";
	private static final String SERVICE_URL = "http://192.168.246.25/CantinaIplService.svc/GetUser/";
	private static final String APP_PASSWORD = "ipl.cantina.1213@gmail.com";

	// [ENDREGION] Constants

	@Override
	protected User doInBackground(String... params) {

		for (String param : params) {
			try {
				JSONArray jsonArray = new JSONArray(readJsonData(SERVICE_URL
						+ param + "," + APP_PASSWORD));

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);

					UserSingleton.getInstance().user = new User(
							jsonObject.getString("Login"),
							jsonObject.getInt("Bi"),
							jsonObject.getString("Name"),
							jsonObject.getString("Course"),
							jsonObject.getBoolean("Regime"),
							jsonObject.getString("Photo"),
							jsonObject.getInt("Nif"),
							jsonObject.getString("Email"),
							jsonObject.getBoolean("Type"),
							jsonObject.getBoolean("Active"),
							jsonObject.getString("School"));
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
		}

		return null;
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
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stringBuilder.toString();
	}

}
