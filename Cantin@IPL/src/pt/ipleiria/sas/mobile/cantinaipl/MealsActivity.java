package pt.ipleiria.sas.mobile.cantinaipl;

import java.util.ArrayList;

import pt.ipleiria.sas.mobile.cantinaipl.controller.HorizontalListView;
import pt.ipleiria.sas.mobile.cantinaipl.controller.MealsListAdapter;
import pt.ipleiria.sas.mobile.cantinaipl.model.Meal;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class MealsActivity extends Activity implements OnItemClickListener {

	private HorizontalListView primaryGallery;
	private HorizontalListView secondaryGallery;
	private ArrayList<Meal> mealsList;
	private MealsListAdapter mealsListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ementas);

		primaryGallery = (HorizontalListView) findViewById(R.id.galleryPrimary);
		primaryGallery.setOnItemClickListener(this);
		secondaryGallery = (HorizontalListView) findViewById(R.id.gallerySecondary);
		secondaryGallery.setOnItemClickListener(this);
		((TextView) findViewById(R.id.textViewPrimary)).setText("- Almoço -");
		((TextView) findViewById(R.id.textViewSecondary)).setText("- Jantar -");

		PopulateList();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void PopulateList() {
		mealsList = new ArrayList<Meal>();

		mealsList.add(new Meal(1, "Salada Russa", "Receita Transmontana",
				R.drawable.cipl_logo, 3, "4ª feira, 23 de abril"));
		mealsList.add(new Meal(2, "Carne de Porco Alentejana",
				"Receita Alentejana", R.drawable.porco_alentejana, 4,
				"5ª feira, 24 de abril"));
		mealsList.add(new Meal(3, "Espetadas de Porco", "Receita Alentejana",
				R.drawable.food_espetadas, 3, "6ª feira, 25 de abril"));
		mealsList.add(new Meal(4, "Salada Russa", "Receita Transmontana",
				R.drawable.cipl_logo, 3, "4ª feira, 23 de abril"));
		mealsList.add(new Meal(5, "Carne de Porco Alentejana",
				"Receita Alentejana", R.drawable.porco_alentejana, 4,
				"5ª feira, 24 de abril"));
		mealsList.add(new Meal(6, "Espetadas de Porco", "Receita Alentejana",
				R.drawable.food_espetadas, 3, "6ª feira, 25 de abril"));
		mealsList.add(new Meal(7, "Salada Russa", "Receita Transmontana",
				R.drawable.cipl_logo, 3, "4ª feira, 23 de abril"));
		mealsList.add(new Meal(8, "Carne de Porco Alentejana",
				"Receita Alentejana", R.drawable.porco_alentejana, 4,
				"5ª feira, 24 de abril"));
		mealsList.add(new Meal(9, "Espetadas de Porco", "Receita Alentejana",
				R.drawable.food_espetadas, 3, "6ª feira, 25 de abril"));

		mealsListAdapter = new MealsListAdapter(this, mealsList);
		primaryGallery.setAdapter(mealsListAdapter);
		secondaryGallery.setAdapter(mealsListAdapter);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		Meal meal = mealsListAdapter.getItem(position);

		Toast.makeText(this, "Foi seleccionada a " + meal.getName(),
				Toast.LENGTH_LONG).show();
	}

}
