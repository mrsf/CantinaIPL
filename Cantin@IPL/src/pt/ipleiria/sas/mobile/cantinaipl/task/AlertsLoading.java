package pt.ipleiria.sas.mobile.cantinaipl.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import pt.ipleiria.sas.mobile.cantinaipl.NotifyMessage;
import pt.ipleiria.sas.mobile.cantinaipl.R;
import pt.ipleiria.sas.mobile.cantinaipl.model.Alert;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressWarnings("deprecation")
public class AlertsLoading extends AsyncTask<String, Alert, LinkedList<Alert>> {

	// [REGION] Constants

	private static final String TAG = "ALERTS_LOADING";
	private static final String SERVICE_URL = "http://192.168.246.25/CantinaIplService.svc";

	// [ENDREGION] Constants

	private int count = 0;

	private final Context context;
	private final NotificationManager notificationManager;

	public AlertsLoading(Context context,
			NotificationManager notificationManager) {
		this.context = context;
		this.notificationManager = notificationManager;
	}

	@Override
	protected LinkedList<Alert> doInBackground(String... params) {

		LinkedList<Alert> alertList = new LinkedList<Alert>();

		for (String param : params) {

			try {
				JSONArray jsonArray = new JSONArray(readJsonData(SERVICE_URL
						+ param));
				Log.i(TAG, "Number of alert elements: " + jsonArray.length());

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);

					this.publishProgress(new Alert(jsonObject.getInt("Id"),
							jsonObject.getString("Name"), jsonObject
									.getString("Description"), jsonObject
									.getString("Date")));
				}
			} catch (Exception e) {
				Log.d(TAG, e.getLocalizedMessage());
				return null;
			}

		}

		return alertList;
	}

	@Override
	protected void onProgressUpdate(Alert... alerts) {

		super.onProgressUpdate(alerts);

		for (Alert alert : alerts) {

			Notification note = new Notification(R.drawable.ic_menu_cantinaipl,
					"Existem notificações Cantin@IPL!",
					System.currentTimeMillis());

			Intent intent = new Intent(context, NotifyMessage.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("notification",
					alert.getDate() + " - " + alert.getDescription());

			PendingIntent i = PendingIntent.getActivity(context, count, intent,
					PendingIntent.FLAG_UPDATE_CURRENT);

			note.setLatestEventInfo(context, alert.getName(), alert.getDate()
					+ " - " + alert.getDescription(), i);

			note.number = ++count;
			note.vibrate = new long[] { 500L, 200L, 200L, 500L };
			note.flags |= Notification.FLAG_AUTO_CANCEL;

			this.notificationManager.notify(alert.getId(), note);

		}
	}

	// [REGION] Methods

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

	// [ENDREGION] Methods

}
