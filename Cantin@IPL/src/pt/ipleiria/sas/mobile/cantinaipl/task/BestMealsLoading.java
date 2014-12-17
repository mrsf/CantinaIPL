package pt.ipleiria.sas.mobile.cantinaipl.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

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
import pt.ipleiria.sas.mobile.cantinaipl.controller.UserSingleton;
import pt.ipleiria.sas.mobile.cantinaipl.model.Dish;
import pt.ipleiria.sas.mobile.cantinaipl.model.Meal;
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
public class BestMealsLoading extends AsyncTask<String, Meal, LinkedList<Meal>> {

	// [REGION] Constants

	private static final String TAG = "BESTMEALS_LOADING";
	private static final String SERVICE_URL = "http://192.168.79.128/CantinaIplService.svc/";

	// [ENDREGION] Constants

	private int count = 0;

	private final Context context;
	private final NotificationManager notificationManager;

	public BestMealsLoading(Context context,
			NotificationManager notificationManager) {
		this.context = context;
		this.notificationManager = notificationManager;
	}

	@Override
	protected LinkedList<Meal> doInBackground(String... params) {

		LinkedList<Meal> mealsList = new LinkedList<Meal>();

		for (String param : params) {

			try {
				JSONArray jsonArray = new JSONArray(readJsonData(SERVICE_URL
						+ param));
				Log.i(TAG, "Number of meals elements: " + jsonArray.length());

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					List<Dish> dishList = new ArrayList<Dish>();
					JSONArray jsonDishArray = jsonObject.getJSONArray("Dishes");
					Log.i(TAG,
							"Number of dish elements: "
									+ jsonDishArray.length());

					for (int j = 0; j < jsonDishArray.length(); j++) {
						JSONObject jsonDishObject = jsonDishArray
								.getJSONObject(j);

						String photo = ((jsonDishObject.getString("Photo")
								.equals("null")) ? jsonDishObject
								.getString("Photo") : "http://192.168.79.128/"
								+ jsonDishObject.getString("Photo"));

						dishList.add(new Dish(jsonDishObject.getInt("Id"),
								photo, jsonDishObject.getString("Description"),
								jsonDishObject.getString("Name"),
								jsonDishObject.getDouble("Price"),
								jsonDishObject.getString("Type"),
								jsonDishObject.getDouble("MyRating")));
					}

					Meal meal = new Meal(
							jsonObject.getInt("Id"),
							jsonObject.getString("Date"),
							jsonObject.getBoolean("Refeicao"),
							jsonObject.getString("Type"),
							dishList,
							(!UserSingleton.getInstance().getUser().getType() ? jsonObject
									.getDouble("StudentPrice") : jsonObject
									.getDouble("EmployeePrice")));

					this.publishProgress(meal);
				}
			} catch (Exception e) {
				Log.d(TAG, e.getLocalizedMessage());
				return null;
			}

		}

		return mealsList;
	}

	@Override
	protected void onProgressUpdate(Meal... meals) {

		super.onProgressUpdate(meals);

		for (Meal meal : meals) {

			if (!meal.getDishes().isEmpty()) {

				List<Dish> d = meal.getDishes();

				Collections.sort(d, new Comparator<Dish>() {
					@Override
					public int compare(Dish lhs, Dish rhs) {
						return lhs.getType().compareTo(rhs.getType());
					};
				});

				Notification note = new Notification(
						R.drawable.ic_menu_cantinaipl,
						"Existem notificações Cantin@IPL!",
						System.currentTimeMillis());

				Intent intent = new Intent(context, NotifyMessage.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("notification",
						d.get(0).getName() + " - " + d.get(0).getDescription()
								+ "(" + String.valueOf(d.get(0).getRating())
								+ ")");

				PendingIntent i = PendingIntent.getActivity(context, count,
						intent, PendingIntent.FLAG_UPDATE_CURRENT);

				note.setLatestEventInfo(context, d.get(0).getName(),
						meal.getDate() + " - " + d.get(0).getDescription(), i);

				note.number = ++count;
				note.vibrate = new long[] { 500L, 200L, 200L, 500L };
				note.flags |= Notification.FLAG_AUTO_CANCEL;

				this.notificationManager.notify(meal.getId(), note);

			}

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
