package pt.ipleiria.sas.mobile.cantinaipl.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pt.ipleiria.sas.mobile.cantinaipl.R;
import pt.ipleiria.sas.mobile.cantinaipl.model.Dish;
import pt.ipleiria.sas.mobile.cantinaipl.model.Meal;
import pt.ipleiria.sas.mobile.cantinaipl.task.ImageDownloader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * @author Márcio Francisco and Mário Correia
 * @version 2013.0609
 * @since 1.0
 * 
 */
public class MealListAdapter extends ListAdapter {

	// [REGION] Constants

	// private static final String TAG = "MEAL_LISTADAPTER";

	// [ENDREGION] Constants

	// [REGION] Variables

	private final int elementWidthSize;
	private final int elementHeightSize;
	private final List<Meal> mealList;

	// [ENDREGION] Variables

	// [REGION] Constructors

	public MealListAdapter(final Context context, ImageCache imageCache,
			ImageDownloader downloader, SynchronizedDownloadList downloadList,
			final int elementWidthSize, final int elementHeightSize,
			final List<Meal> mealList) {
		super(context, imageCache, downloadList, downloader);
		this.elementWidthSize = elementWidthSize;
		this.elementHeightSize = elementHeightSize;
		this.mealList = mealList;
	}

	// [ENDREGION] Constructors

	// [REGION] Inherited_Methods

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

		final LayoutParams iv_layoutParams;
		final LayoutParams fl_layoutParams;

		Meal meal = this.mealList.get(position);
		ImageView imageView = null;
		FrameLayout frameLayout = null;

		if (convertView != null)
			this.removeTaskSafely((ImageView) convertView
					.findViewById(R.id.iv_meal_image));

		if (convertView == null)
			convertView = LayoutInflater.from(super.getContext()).inflate(
					R.layout.item_meal, null);

		imageView = (ImageView) convertView.findViewById(R.id.iv_meal_image);
		iv_layoutParams = imageView.getLayoutParams();
		if (iv_layoutParams != null) {
			iv_layoutParams.width = this.elementWidthSize;
			iv_layoutParams.height = this.elementHeightSize;
		}

		frameLayout = ((FrameLayout) convertView
				.findViewById(R.id.meal_loading));
		fl_layoutParams = frameLayout.getLayoutParams();
		if (fl_layoutParams != null) {
			fl_layoutParams.width = this.elementWidthSize;
			fl_layoutParams.height = this.elementHeightSize;
		}

		((TextView) convertView.findViewById(R.id.tv_meal_date)).setText(meal
				.getDate());

		if (!meal.getDishes().isEmpty()) {

			List<Dish> d = meal.getDishes();

			Collections.sort(d, new Comparator<Dish>() {
				@Override
				public int compare(Dish lhs, Dish rhs) {
					return lhs.getType().compareTo(rhs.getType());
				};
			});

			((TextView) convertView.findViewById(R.id.tv_meal_name)).setText(d
					.get(0).getName());
			((TextView) convertView.findViewById(R.id.tv_meal_desc)).setText(d
					.get(0).getDescription());

			RatingBar rb = ((RatingBar) convertView
					.findViewById(R.id.rb_meal_rating));
			if (d.get(0).getRating() > 0f) {
				rb.setVisibility(View.VISIBLE);
				rb.setNumStars((int) Math.ceil(d.get(0).getRating())); // ceil -
																		// the
																		// value
																		// ever
																		// is
																		// rounded
																		// to up
				rb.setRating((float) d.get(0).getRating());
			} else
				rb.setVisibility(View.GONE);

			imageView.setImageBitmap(null); // Loading
											// image

			if (d.get(0).getPhoto().equals("null")) {
				imageView.setImageResource(R.drawable.cipl_background);
				frameLayout.setVisibility(View.GONE);
			} else if (super.getImageCache().containsElement(
					d.get(0).getPhoto())) {
				imageView.setImageBitmap(super.getImageCache().getElement(
						d.get(0).getPhoto()));
				frameLayout.setVisibility(View.GONE);
			} else
				this.addTaskSafely(new DownloadTask(d.get(0).getPhoto(),
						imageView, frameLayout));

			super.startImageDownloader();

		}

		return convertView;
	}

	// [ENDREGION] Inherited_Methods

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
