package pt.ipleiria.sas.mobile.cantinaipl;

import java.util.LinkedList;

import com.actionbarsherlock.view.MenuInflater;

import pt.ipleiria.sas.mobile.cantinaipl.controller.EventSingleton;
import pt.ipleiria.sas.mobile.cantinaipl.controller.HorizontalListView;
import pt.ipleiria.sas.mobile.cantinaipl.controller.MealListAdapter;
import pt.ipleiria.sas.mobile.cantinaipl.controller.UserSingleton;
import pt.ipleiria.sas.mobile.cantinaipl.model.Meal;
import pt.ipleiria.sas.mobile.cantinaipl.task.MealsLoading;

import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

/**
 * @author Márcio Francisco and Mário Correia
 * @version 2013.0612
 * @since 1.0
 * 
 */
public class MealsActivity extends BaseActivity implements OnItemClickListener {

	private static final String TAG = "MEALS_ACTIVITY";
	private static final String SERVICE_METHOD = "/"
			+ UserSingleton.getInstance().getUser().getLogin()
			+ "$ipl.cantina.1213@gmail.com$";

	private HorizontalListView primaryGallery;
	private MealListAdapter primaryMealListAdapter;
	private LinkedList<Meal> primaryMealList;
	private MealsLoading primaryMealsLoading;

	private HorizontalListView secondaryGallery;
	private MealListAdapter secondaryMealListAdapter;
	private LinkedList<Meal> secondaryMealList;
	private MealsLoading secondaryMealsLoading;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meals);

		primaryGallery = (HorizontalListView) findViewById(R.id.primary_gallery);
		primaryGallery.setOnItemClickListener(this);
		((TextView) findViewById(R.id.primary_text))
				.setText("- Ementa de Almoço -");

		secondaryGallery = (HorizontalListView) findViewById(R.id.secondary_gallery);
		secondaryGallery.setOnItemClickListener(this);
		((TextView) findViewById(R.id.secondary_text))
				.setText("- Ementa de Jantar -");

		Bundle bundle = getIntent().getBundleExtra("canteen");

		com.actionbarsherlock.app.ActionBar ab = getSupportActionBar();
		ab.setTitle(bundle.getString("name"));

		EventSingleton.getInstance().getEvent()
				.setLocation(bundle.getString("name"));

		populateLists(String.valueOf(bundle.getInt("id")));
	}

	private void populateLists(String canteenId) {

		DisplayMetrics dm = getResources().getDisplayMetrics();

		int firstItemWidthSize = (int) Math
				.round((dm.widthPixels < dm.heightPixels) ? dm.widthPixels * 0.8
						: dm.widthPixels * 0.33);
		int firstItemHeightSize = (int) Math
				.round((firstItemWidthSize * 3) / 4);
		int secondItemWidthSize = (int) Math
				.round((dm.widthPixels < dm.heightPixels) ? dm.widthPixels * 0.61
						: dm.widthPixels * 0.27);
		int secondItemHeightSize = (int) Math
				.round((secondItemWidthSize * 3) / 4);

		primaryMealList = new LinkedList<Meal>();
		primaryMealListAdapter = new MealListAdapter(this,
				super.getImageCache(), super.getImageDownloader(),
				super.getDownloadList(), firstItemWidthSize,
				firstItemHeightSize, primaryMealList);
		primaryGallery.setAdapter(primaryMealListAdapter);

		secondaryMealList = new LinkedList<Meal>();
		secondaryMealListAdapter = new MealListAdapter(this,
				super.getImageCache(), super.getImageDownloader(),
				super.getDownloadList(), secondItemWidthSize,
				secondItemHeightSize, secondaryMealList);
		secondaryGallery.setAdapter(secondaryMealListAdapter);

		if (this.primaryMealsLoading == null) {
			Log.i(TAG, "Thread to load lunch meals data is running.");
			this.primaryMealsLoading = new MealsLoading(this,
					this.primaryMealList, this.primaryMealListAdapter);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				this.primaryMealsLoading.executeOnExecutor(super.getExec(),
						SERVICE_METHOD + "false$" + canteenId);
			else
				this.primaryMealsLoading.execute(SERVICE_METHOD + "false$"
						+ canteenId);
		}

		if (this.secondaryMealsLoading == null) {
			Log.i(TAG, "Thread to load dinner meals data is running.");
			this.secondaryMealsLoading = new MealsLoading(this,
					this.secondaryMealList, this.secondaryMealListAdapter);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				this.secondaryMealsLoading.executeOnExecutor(super.getExec(),
						SERVICE_METHOD + "true$" + canteenId);
			else
				this.secondaryMealsLoading.execute(SERVICE_METHOD + "true$"
						+ canteenId);
		}

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

		Meal meal = ((Meal) adapter.getAdapter().getItem(position));

		/*
		 * Toast.makeText(this, "Foi seleccionada a " +
		 * meal.getDishes().get(0).getName(), Toast.LENGTH_LONG).show();
		 */

		Meal m = new Meal();
		m.setId(meal.getId());
		m.setDate(meal.getDate());
		m.setType(meal.getType());
		m.setDishes(meal.getDishes());
		m.setPrice(meal.getPrice());

		Intent intent = new Intent(this, MealDetailsActivity.class);
		intent.putExtra("meal", m);

		startActivity(intent);
	}

}
