package pt.ipleiria.sas.mobile.cantinaipl.service;

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
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import pt.ipleiria.sas.mobile.cantinaipl.MainActivity;
import pt.ipleiria.sas.mobile.cantinaipl.model.User;

public class AuthenticationService {

	private static final String TAG = "JSON";
	private static final String SERVICE_URL = "http://25.42.150.3/CantinaIplService.svc";

	public AuthenticationService(User user, Context context) {
		new ReadJsonDataTask(context).execute(SERVICE_URL
				+ "/UserAuthentication/" + user.getUsername() + ","
				+ user.getPassword());
	}

	public String readJsonData(String serviceUrl) {
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

	private class ReadJsonDataTask extends AsyncTask<String, Void, String> {

		private final Context context;

		protected ReadJsonDataTask(Context context) {
			this.context = context;
		}

		protected String doInBackground(String... urls) {
			return readJsonData(urls[0]);
		}

		protected void onPostExecute(String result) {
			try {
				JSONArray jsonArray = new JSONArray(result);
				Log.i("JSON",
						"Number of surveys in feed: " + jsonArray.length());

				// ---print out the content of the json feed---
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);

					MainActivity mainActivity = (MainActivity) context;
					mainActivity.testAuthentication(jsonObject
							.getBoolean("Result"), jsonObject.getString("Messenge"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
