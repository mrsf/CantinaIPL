package pt.ipleiria.sas.mobile.cantinaipl.controller;

import java.util.List;

import pt.ipleiria.sas.mobile.cantinaipl.R;
import pt.ipleiria.sas.mobile.cantinaipl.model.Meal;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SearchListAdapter extends BaseAdapter {

	private final Context context;
	private final List<Meal> mealList;

	public SearchListAdapter(final Context context, final List<Meal> mealList) {
		this.context = context;
		this.mealList = mealList;
	}

	@Override
	public int getCount() {
		return mealList.size();
	}

	@Override
	public Meal getItem(int position) {
		return mealList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Meal meal = this.mealList.get(position);

		if (convertView == null)
			convertView = LayoutInflater.from(this.context).inflate(
					R.layout.search_item, null);

		((TextView) convertView.findViewById(R.id.tv_search_meal_name))
				.setText(meal.getDishes().get(0).getName());
		((TextView) convertView.findViewById(R.id.tv_search_meal_desc))
				.setText(meal.getDishes().get(0).getDescription());
		((TextView) convertView.findViewById(R.id.tv_search_meal_date))
		.setText(String.valueOf(meal.getId()));

		return convertView;
	}

}
