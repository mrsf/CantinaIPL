package pt.ipleiria.sas.mobile.cantinaipl;

import java.util.LinkedList;

import com.actionbarsherlock.view.MenuInflater;

import pt.ipleiria.sas.mobile.cantinaipl.controller.EventSingleton;
import pt.ipleiria.sas.mobile.cantinaipl.controller.SearchListAdapter;
import pt.ipleiria.sas.mobile.cantinaipl.database.MealsRepository;
import pt.ipleiria.sas.mobile.cantinaipl.model.Meal;
import pt.ipleiria.sas.mobile.cantinaipl.task.MealsSearch;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class SearchActivity extends BaseActivity implements OnItemClickListener {

	// [REGION] Constants

	private static final String TAG = "SEARCH_ACTIVITY";

	// [ENDREGION] Constants

	private TextView listCount;
	private ListView searchListView;
	private LinkedList<Meal> searchMealList;
	private SearchListAdapter searchListAdapter;
	private MealsSearch mealsSearch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		// Log.i("SEARCH", getIntent().getStringExtra("search"));

		searchListView = (ListView) findViewById(R.id.lv_search_meals);
		searchListView.setOnItemClickListener(this);

		searchMealList = new LinkedList<Meal>();
		searchListAdapter = new SearchListAdapter(this, searchMealList,
				super.getImageCache(), super.getImageDownloader(),
				super.getDownloadList());
		searchListView.setAdapter(searchListAdapter);
		listCount = (TextView) findViewById(R.id.tv_list_count);

		if (this.mealsSearch == null) {
			Log.i(TAG, "Thread to search meals is running.");
			this.mealsSearch = new MealsSearch(this, this.searchMealList,
					this.searchListAdapter, this.listCount);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				this.mealsSearch.executeOnExecutor(super.getExec(), getIntent()
						.getStringExtra("search"));
			else
				this.mealsSearch.execute(getIntent().getStringExtra("search"));
		}

		EventSingleton.getInstance().getEvent().setLocation("Cantina 1");

	}

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

		MealsRepository mealsRepository = new MealsRepository(
				getApplicationContext(), false);
		mealsRepository.open();
		Meal m = mealsRepository.getMealById(meal.getId());
		mealsRepository.close();

		Intent intent = new Intent(this, MealDetailsActivity.class);
		intent.putExtra("meal", m);

		startActivity(intent);
	}

}
