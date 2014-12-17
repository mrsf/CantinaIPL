package pt.ipleiria.sas.mobile.cantinaipl.controller;

import java.util.List;

import pt.ipleiria.sas.mobile.cantinaipl.R;
import pt.ipleiria.sas.mobile.cantinaipl.model.Meal;
import pt.ipleiria.sas.mobile.cantinaipl.task.ImageDownloader;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class SearchListAdapter extends ListAdapter {

	private final List<Meal> mealList;

	public SearchListAdapter(final Context context, final List<Meal> mealList,
			ImageCache imageCache, ImageDownloader downloader,
			SynchronizedDownloadList downloadList) {
		super(context, imageCache, downloadList, downloader);
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
		ImageView imageView = null;
		FrameLayout frameLayout = null;

		if (convertView != null)
			this.removeTaskSafely((ImageView) convertView
					.findViewById(R.id.iv_search_meal_photo));
		
		if (convertView == null)
			convertView = LayoutInflater.from(super.getContext()).inflate(
					R.layout.search_item, null);

		imageView = (ImageView) convertView.findViewById(R.id.iv_search_meal_photo);
		frameLayout = ((FrameLayout) convertView
				.findViewById(R.id.search_meal_loading));

		((TextView) convertView.findViewById(R.id.tv_search_meal_name))
				.setText(meal.getDishes().get(0).getName());
		((TextView) convertView.findViewById(R.id.tv_search_meal_desc))
				.setText(meal.getDishes().get(0).getDescription());
		((TextView) convertView.findViewById(R.id.tv_search_meal_date))
				.setText(String.valueOf(meal.getDate()));

		imageView.setImageBitmap(null); // Loading
		// image

		if (meal.getDishes().get(0).getPhoto().equals("null")) {
			imageView.setImageResource(R.drawable.cipl_background);
			frameLayout.setVisibility(View.GONE);
		} else if (super.getImageCache().containsElement(meal.getDishes().get(0).getPhoto())) {
			imageView.setImageBitmap(super.getImageCache().getElement(
					meal.getDishes().get(0).getPhoto()));
			frameLayout.setVisibility(View.GONE);
		} else
			this.addTaskSafely(new DownloadTask(meal.getDishes().get(0).getPhoto(), imageView,
					frameLayout));
		super.startImageDownloader();

		return convertView;
	}
	
	// [REGION] Methods

	private void addTaskSafely(final DownloadTask downloadTask) {

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					getDownloadList().addDownloadTask(downloadTask);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	private void removeTaskSafely(final ImageView image) {

		new Thread(new Runnable() {
			@Override
			public void run() {
				getDownloadList().removeDownloadTaskByImageView(image);
			}
		}).start();

	}

	// [ENDREGION] Methods

}
