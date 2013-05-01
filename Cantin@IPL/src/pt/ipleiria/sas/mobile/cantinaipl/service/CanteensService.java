package pt.ipleiria.sas.mobile.cantinaipl.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import pt.ipleiria.sas.mobile.cantinaipl.CanteensActivity;
import pt.ipleiria.sas.mobile.cantinaipl.controller.CanteenListAdapter;
import pt.ipleiria.sas.mobile.cantinaipl.database.CanteensRepository;
import pt.ipleiria.sas.mobile.cantinaipl.model.Canteen;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class CanteensService {

	private static final String TAG = "JSON";
	private static final String SERVICE_URL = "http://25.42.150.3/CantinaIplService.svc";
	private final CanteenListAdapter canteensListAdapter;

	public CanteensService(Context context,
			CanteensRepository canteensRepository,
			CanteenListAdapter canteensListAdapter) {
		this.canteensListAdapter = canteensListAdapter;
		new ReadJsonDataTask(context, canteensRepository).execute(SERVICE_URL
				+ "/GetCanteens");
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

	private class ReadJsonDataTask extends AsyncTask<String, String, String> {

		private final Context context;
		private final CanteensRepository canteensRepository;

		protected ReadJsonDataTask(Context context,
				CanteensRepository canteensRepository) {
			this.context = context;
			this.canteensRepository = canteensRepository;
		}

		protected String doInBackground(String... urls) {
			return readJsonData(urls[0]);
		}

		protected void onPostExecute(String result) {
			try {
				ArrayList<Canteen> canteensList = new ArrayList<Canteen>();
				JSONArray jsonArray = new JSONArray(result);
				Log.i("JSON",
						"Number of surveys in feed: " + jsonArray.length());

				CanteensActivity canteensActivity = (CanteensActivity) context;
				canteensRepository.open();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);

					canteensList.add(new Canteen(
							jsonObject.getInt("Canteenid"), jsonObject
									.getString("Name"), jsonObject
									.getString("Address"), jsonObject
									.getString("LunchHorary"), jsonObject
									.getString("DinnerHorary"), jsonObject
									.getString("Campus"), jsonObject
									.getString("Photo"), jsonObject
									.getDouble("Latitude"), jsonObject
									.getDouble("Longitude")));

					canteensRepository.InsertCanteens(canteensList);

					canteensActivity.setCanteensList(canteensRepository
							.GetCanteens());

					canteensListAdapter.setCanteenList(canteensActivity
							.getCanteensList());

					// canteensListAdapter.notifyDataSetChanged();
				}
				// CanteensActivity canteensActivity = (CanteensActivity)
				// context;
				// canteensActivity.populateCanteensList();
				canteensRepository.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
