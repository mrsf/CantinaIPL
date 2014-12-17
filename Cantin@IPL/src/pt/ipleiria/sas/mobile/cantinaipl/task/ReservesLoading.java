package pt.ipleiria.sas.mobile.cantinaipl.task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import pt.ipleiria.sas.mobile.cantinaipl.controller.ReserveListAdapter;
import pt.ipleiria.sas.mobile.cantinaipl.model.Dish;
import pt.ipleiria.sas.mobile.cantinaipl.model.Reserve;

/**
 * <b>Reserves loading task class.</b>
 * 
 * <p>
 * This class allows the initialization of a task, to load a user reserves.
 * </p>
 * 
 * @author Márcio Francisco and Mário Correia
 * @version 2013.0701
 * @since 1.0
 * 
 */
// public class ReservesLoading extends
// DataLoading<String, Reserva, LinkedList<Reserva>> {
public class ReservesLoading extends
		DataLoading<String, Reserve, LinkedList<Reserve>> {

	// [REGION] Constants

	private static final String TAG = "RESERVES_LOADING";
	private static final String SERVICE_METHOD = getServiceUrl()
			+ "GetReserves/";

	// [ENDREGION] Constants

	// [REGION] Fields

	private final ReserveListAdapter reserveListAdapter;

	// private LinkedList<Reserva> reserveList;
	private LinkedList<Reserve> reserveList;

	/* private ReservesRepository reservesRepository; */

	// [ENDREGION] Fields

	// [REGION] Constructors

	// public ReservesLoading(final Context context,
	// LinkedList<Reserva> reserveList,
	// ReserveListAdapter reserveListAdapter) {
	public ReservesLoading(final Context context,
			LinkedList<Reserve> reserveList,
			ReserveListAdapter reserveListAdapter) {
		super(context);
		this.reserveList = reserveList;
		this.reserveListAdapter = reserveListAdapter;
		/* this.reservesRepository = new ReservesRepository(context, false); */
	}

	// [ENDREGION] Constructors

	// [REGION] Inherited_Methods

	@Override
	protected LinkedList<Reserve> doInBackground(String... params) {

		/*
		 * this.reservesRepository.open();
		 * this.reservesRepository.getUnusedReserves(userName);
		 * this.reservesRepository.close();
		 */

		for (String param : params) {
			try {
				JSONArray jsonArray = new JSONArray(
						super.readJsonData(SERVICE_METHOD + param));
				Log.i(TAG, "Number of Reserve elements: " + jsonArray.length());

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);

					List<Dish> dishes = new ArrayList<Dish>();
					JSONArray jsonDishArray = jsonObject
							.getJSONArray("ListDish");

					for (int j = 0; j < jsonDishArray.length(); j++) {
						JSONObject jsonDishObject = jsonDishArray
								.getJSONObject(j);

						String photo = ((jsonDishObject.getString("Photo")
								.equals("null")) ? jsonDishObject
								.getString("Photo") : super.getServerUrl()
								+ jsonDishObject.getString("Photo"));

						dishes.add(new Dish(jsonDishObject.getInt("Id"), photo,
								jsonDishObject.getString("Description"),
								jsonDishObject.getString("Name"),
								jsonDishObject.getDouble("Price"),
								jsonDishObject.getString("Type"),
								jsonDishObject.getDouble("MyRating")));
					}

					Reserve reserva = new Reserve(jsonObject.getInt("Id"),
							jsonObject.getString("PurchaseDate"),
							jsonObject.getString("UseDate"),
							jsonObject.getDouble("Price"),
							jsonObject.getBoolean("IsValid"),
							jsonObject.getString("UserLogin"),
							jsonObject.getInt("MealId"),
							jsonObject.getBoolean("IsAccounted"), dishes,
							jsonObject.getInt("CanteenId"),
							jsonObject.getString("TypeReserve"));

					publishProgress(reserva);
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
		}

		return null;
	}

	// protected void onProgressUpdate(Reserva... values) {
	@Override
	protected void onProgressUpdate(Reserve... values) {
		super.onProgressUpdate(values);
		for (Reserve reserva : values)
			reserveList.add(reserva);

		reserveListAdapter.notifyDataSetChanged();
	}

	// [ENDREGION] Inherited_Methods

}
