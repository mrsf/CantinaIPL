package pt.ipleiria.sas.mobile.cantinaipl.controller;

import java.util.List;

import pt.ipleiria.sas.mobile.cantinaipl.R;
import pt.ipleiria.sas.mobile.cantinaipl.model.Meal;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class MealsListAdapter extends BaseAdapter {

	// [REGION] Fields

	private Context context;
	private List<Meal> mealsList;

	//private ImagesManager imagesManager;

	// [ENDREGION] Fields

	// [REGION] Constructors

	public MealsListAdapter(Context context, List<Meal> mealsList) {
		super();
		this.context = context;
		this.mealsList = mealsList;
		// imagesManager = new ImagesManager();
	}

	// [ENDREGION] Constructors

	// [REGION] Inherited

	@Override
	public int getCount() {
		return mealsList.size();
	}

	@Override
	public Meal getItem(int position) {
		return mealsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Meal meal = mealsList.get(position);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(R.layout.item_ementas, null);

		/*Activity activity = (Activity) context;
		DisplayMetrics metrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

		LayoutParams layoutParams = new LayoutParams(
				400 * (metrics.densityDpi / 160), LayoutParams.MATCH_PARENT);
		convertView.setLayoutParams(layoutParams);

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i("IPL", "-----------------------------" + position);
			}
		});*/

		((TextView) convertView.findViewById(R.id.textViewDate)).setText(meal
				.getDate());
		((TextView) convertView.findViewById(R.id.textViewName)).setText(meal
				.getName());
		((TextView) convertView.findViewById(R.id.textViewDesc)).setText(meal
				.getDescription());

		((RatingBar) convertView.findViewById(R.id.ratingBarMeal))
				.setRating((float) meal.getRacking());

		
		  /*if (!imagesManager.getImagesHashMap().containsKey(meal.getPhoto())) {
		  imagesManager.getImagesHashMap().put(meal.getPhoto(),
		  BitmapFactory.decodeFile(meal.getPhoto(), null));
		  imagesManager.getImagesList().add(meal.getPhoto()); }*/
		 

		((ImageView) convertView.findViewById(R.id.imageViewMeal))
				.setImageResource(meal.getPhoto());

		return convertView;
	}

	// [ENDREGION] Inherited

}
