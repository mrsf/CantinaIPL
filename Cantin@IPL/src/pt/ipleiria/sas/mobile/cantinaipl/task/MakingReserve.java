package pt.ipleiria.sas.mobile.cantinaipl.task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pt.ipleiria.sas.mobile.cantinaipl.CantinaIplApplication;
import pt.ipleiria.sas.mobile.cantinaipl.controller.EventSingleton;
import pt.ipleiria.sas.mobile.cantinaipl.database.ReservesRepository;
import pt.ipleiria.sas.mobile.cantinaipl.model.CalendarEvent;
import pt.ipleiria.sas.mobile.cantinaipl.model.Dish;
import pt.ipleiria.sas.mobile.cantinaipl.model.Reserve;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.util.Log;
import android.widget.Toast;

/**
 * <b>Making reserve task class.</b>
 * 
 * <p>
 * This class allows the initialization of a task, to make a reserve, chose for
 * a user on application.
 * </p>
 * 
 * @author Márcio Francisco and Mário Correia
 * @version 2013.0701
 * @since 1.0
 * 
 */
public class MakingReserve extends DataLoading<String, Boolean, Reserve> {

	// [REGION] Constants

	private static final String TAG = "MAKING_RESERVE";
	private static final String SERVICE_METHOD = getServiceUrl()
			+ "MakeReserve/";

	// [ENDREGION] Constants

	// [REGION] Variables

	private final CantinaIplApplication cantinaIplApplication;
	private ProgressDialog progressDialog;

	// [ENDREGION] Variables

	// [REGION] Constructors

	public MakingReserve(final Context context, ProgressDialog progressDialog) {
		super(context);
		this.cantinaIplApplication = (CantinaIplApplication) context
				.getApplicationContext();
		this.progressDialog = progressDialog;
	}

	// [ENDREGION] Constructors

	// [REGION] Inherited_Methods

	@Override
	protected Reserve doInBackground(String... params) {

		Reserve reserve = null;

		for (String param : params) {
			try {
				JSONObject jsonObject = new JSONObject(
						super.readJsonData(SERVICE_METHOD + param));
				Log.i(TAG, "Making a reserve.");

				List<Dish> dishes = new ArrayList<Dish>();
				JSONArray jsonDishArray = jsonObject.getJSONArray("ListDish");

				for (int j = 0; j < jsonDishArray.length(); j++) {
					JSONObject jsonDishObject = jsonDishArray.getJSONObject(j);

					String photo = ((jsonDishObject.getString("Photo")
							.equals("null")) ? jsonDishObject
							.getString("Photo") : "http://25.42.150.3/"
							+ jsonDishObject.getString("Photo"));

					dishes.add(new Dish(jsonDishObject.getInt("Id"), photo,
							jsonDishObject.getString("Description"),
							jsonDishObject.getString("Name"), jsonDishObject
									.getDouble("Price"), jsonDishObject
									.getString("Type"), jsonDishObject
									.getDouble("Rating")));
				}

				reserve = new Reserve(jsonObject.getInt("Id"),
						jsonObject.getString("PurchaseDate"),
						jsonObject.getString("UseDate"),
						jsonObject.getDouble("Price"),
						jsonObject.getBoolean("IsValid"),
						jsonObject.getString("UserLogin"),
						jsonObject.getInt("MealId"),
						jsonObject.getBoolean("IsAccounted"), dishes,
						jsonObject.getInt("CanteenId"),
						jsonObject.getString("TypeReserve"));

			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
		}

		return reserve;
	}

	@Override
	protected void onPostExecute(Reserve reserve) {
		super.onPostExecute(reserve);

		if (reserve != null) {
			ReservesRepository reservesRepository = new ReservesRepository(
					super.getContext(), false);
			reservesRepository.open();
			reservesRepository.insertReserve(reserve);
			reservesRepository.close();

			this.progressDialog.dismiss();
			Toast.makeText(super.getContext(),
					"Reserva efectuada com sucesso.", Toast.LENGTH_SHORT)
					.show();

			if (cantinaIplApplication.isCalendarEventsActive())
				createCalendarEvent();
		} else {
			this.progressDialog.dismiss();
			Toast.makeText(super.getContext(),
					"Não é possível efectuar a reserva!", Toast.LENGTH_SHORT)
					.show();
		}
	}

	// [ENDREGION] Inherited_Methods

	// [REGION] Methods

	private void createCalendarEvent() {

		long startMillis = 0;
		long endMillis = 0;
		Calendar beginTime = Calendar.getInstance();
		beginTime.set(2013, 7, 8, 12, 00);
		startMillis = beginTime.getTimeInMillis();
		Calendar endTime = Calendar.getInstance();
		endTime.set(2013, 7, 8, 14, 30);
		endMillis = endTime.getTimeInMillis();

		CalendarEvent calendarEvent = EventSingleton.getInstance().getEvent();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
			this.createNewestCalendarEvent(calendarEvent, startMillis,
					endMillis);
		else
			this.createOldestCalendarEvent(calendarEvent, startMillis,
					endMillis);

	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private void createNewestCalendarEvent(CalendarEvent calendarEvent,
			long startMillis, long endMillis) {

		Intent intent = new Intent(Intent.ACTION_INSERT)
				.setData(Events.CONTENT_URI)
				.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
				.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis)
				.putExtra(Events.TITLE, calendarEvent.getTitle())
				.putExtra(
						Events.DESCRIPTION,
						calendarEvent.getUserType() + " "
								+ calendarEvent.getRefeicaoType() + " - "
								+ calendarEvent.getDescription())
				.putExtra(Events.EVENT_LOCATION, calendarEvent.getLocation())
				.putExtra(Events.HAS_ALARM, true)
				.putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY);
		super.getContext().startActivity(intent);

	}

	private void createOldestCalendarEvent(CalendarEvent calendarEvent,
			long startMillis, long endMillis) {

		Intent intent = new Intent(Intent.ACTION_EDIT);
		intent.setType("vnd.android.cursor.item/event");
		intent.putExtra("beginTime", startMillis);
		intent.putExtra("allDay", false);
		// intent.putExtra("rrule", "FREQ=YEARLY");
		intent.putExtra("endTime", endMillis);
		intent.putExtra("title", calendarEvent.getTitle());
		intent.putExtra(
				"description",
				calendarEvent.getUserType() + " "
						+ calendarEvent.getRefeicaoType() + " - "
						+ calendarEvent.getDescription());
		intent.putExtra("eventLocation", calendarEvent.getLocation());
		intent.putExtra("hasAlarm", true);
		super.getContext().startActivity(intent);

	}

	// [ENDREGION] Methods

}
