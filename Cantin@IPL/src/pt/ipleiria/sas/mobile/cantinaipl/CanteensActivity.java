package pt.ipleiria.sas.mobile.cantinaipl;

import java.util.LinkedList;

import com.actionbarsherlock.view.MenuInflater;

import pt.ipleiria.sas.mobile.cantinaipl.controller.CanteenListAdapter;
import pt.ipleiria.sas.mobile.cantinaipl.controller.EventSingleton;
import pt.ipleiria.sas.mobile.cantinaipl.controller.UserSingleton;
import pt.ipleiria.sas.mobile.cantinaipl.model.Canteen;
import pt.ipleiria.sas.mobile.cantinaipl.model.User;
import pt.ipleiria.sas.mobile.cantinaipl.task.AlertsLoading;
import pt.ipleiria.sas.mobile.cantinaipl.task.CanteensLoading;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.NotificationManager;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Canteen List Activity. This class allows you to fill the view with the list
 * of canteens.
 * 
 * @author Márcio Francisco and Mário Correia
 * @version 2013.0627
 * @since 1.0
 * 
 */
public class CanteensActivity extends ClosableActivity implements
		OnItemClickListener {

	// [REGION] Constants

	private static final String TAG = "CANTEENS_ACTIVITY";
	private static final String SERVICE_METHOD = "/ipl.cantina.1213@gmail.com";

	// [ENDREGION] Constants

	// [REGION] Fields

	private ListView canteenListView;
	private CanteenListAdapter canteenListAdapter;
	private LinkedList<Canteen> canteenList;
	private CanteensLoading canteensLoading;
	private NotificationManager notificationManager;
	private AlertsLoading alertLoading;

	// [ENDREGION] Fields

	// [REGION] Inherited_Methods

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_canteens);

		User user = UserSingleton.getInstance().getUser();
		EventSingleton.getInstance().getEvent()
				.setUserType(user.isType() ? "Funcionário" : "Estudante");

		this.notificationManager = null;

		this.canteenListView = (ListView) findViewById(R.id.canteen_list);
		this.canteenListView.setDivider(null);
		this.canteenListView.setOnItemClickListener(this);

		if (savedInstanceState == null)
			this.populateList();
		else
			this.canteenListView
					.setAdapter((CanteenListAdapter) savedInstanceState
							.getParcelable("adapter"));
	}

	public void onMapClick(View v) {

		try {
			Canteen canteen = (Canteen) v.getTag();
			Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
					Uri.parse("google.navigation:ll=" + canteen.getLatitude()
							+ "," + canteen.getLongitude() + "&mode=w"));
			startActivity(intent);
		} catch (Exception e) {
			Log.w(TAG, e.getLocalizedMessage());
			super.DisplayToast("Informação não disponível!");
		}
	}

	/*
	 * @Override
	 * 
	 * @Deprecated public Object getLastNonConfigurationInstance() { return
	 * this.canteenListAdapter; }
	 */

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putParcelable("adapter", this.canteenListAdapter);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		this.canteenListAdapter = (CanteenListAdapter) savedInstanceState
				.getParcelable("adapter");
	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
	 * menu; this adds items to the action bar if it is present.
	 * getMenuInflater().inflate(R.menu.options_menu, menu);
	 * 
	 * return super.onCreateOptionsMenu(menu); }
	 */

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.options_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position,
			long id) {

		Canteen canteen = (Canteen) adapter.getAdapter().getItem(position);

		Toast.makeText(this, "Foi seleccionada a " + canteen.getName(),
				Toast.LENGTH_LONG).show();

		Bundle bundle = new Bundle();
		bundle.putInt("id", canteen.getId());
		bundle.putString("name", canteen.getName());

		Intent intent = new Intent(this, MealsActivity.class);
		intent.putExtra("canteen", bundle);

		startActivity(intent);
	}

	// [ENDREGION] Inherited_Methods

	// [REGION] Methods

	private void populateList() {

		if (((CantinaIplApplication) this.getApplicationContext())
				.isNotificationsActive()) {
			this.notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			Log.i(TAG, "Thread to load canteens data is running.");
			this.alertLoading = new AlertsLoading(this,
					this.notificationManager);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				this.alertLoading.executeOnExecutor(super.getExec(),
						"/GetAlerts/ipl.cantina.1213@gmail.com");
			/*
			 * else this.alertLoading
			 * .execute("/GetAlerts/ipl.cantina.1213@gmail.com");
			 */
		}

		this.canteenList = new LinkedList<Canteen>();
		this.canteenListAdapter = new CanteenListAdapter(this,
				super.getImageCache(), super.getImageDownloader(),
				super.getDownloadList(), this.canteenList);
		this.canteenListView.setAdapter(this.canteenListAdapter);

		if (this.canteensLoading == null) {
			Log.i(TAG, "Thread to load canteens data is running.");
			this.canteensLoading = new CanteensLoading(this, this.canteenList,
					this.canteenListAdapter);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				this.canteensLoading.executeOnExecutor(super.getExec(),
						SERVICE_METHOD);
			else
				this.canteensLoading.execute(SERVICE_METHOD);
			/*BaseActivity
			.getOldExec()
			.executeOnExecutor(
					(AsyncTask<String, Canteen, LinkedList<Canteen>>) this.canteensLoading,
					(Object) SERVICE_METHOD);*/
		}

	}
	// [ENDREGION] Methods

}
