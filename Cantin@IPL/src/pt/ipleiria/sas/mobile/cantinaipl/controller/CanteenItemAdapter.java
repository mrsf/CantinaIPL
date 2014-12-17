package pt.ipleiria.sas.mobile.cantinaipl.controller;

import pt.ipleiria.sas.mobile.cantinaipl.R;
import pt.ipleiria.sas.mobile.cantinaipl.model.Canteen;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Class to custom the canteen list items.
 * 
 * @author Márcio Francisco and Mário Correia
 * @version 2013.0606
 * @since 1.0
 * 
 */
public class CanteenItemAdapter extends BaseAdapter {

	// [REGION] Constants

	private static final String TAG = "CANTEEN_ITEMADAPTER";

	// [ENDREGION] Constants

	// [REGION] Final_Variables

	private final Context context;
	private final Canteen canteen;

	// [ENDREGION] Final_Variables

	// [REGION] Variables

	private ImageCache imageCache;
	private SynchronizedDownloadList downloadList;

	// [ENDREGION] Variables

	// [REGION] Constructors

	public CanteenItemAdapter(final Context context, final Canteen canteen,
			ImageCache imageCache, SynchronizedDownloadList downloadList) {
		super();
		this.context = context;
		this.canteen = canteen;
		this.imageCache = imageCache;
		this.downloadList = downloadList;
	}

	// [ENDREGION] Constructors

	// [REGION] Inherited_Methods

	@Override
	public int getCount() {
		return 2;
	}

	@Override
	public Canteen getItem(int position) {
		return this.canteen;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView != null)
			this.removeTaskSafely((ImageView) convertView
					.findViewById(R.id.canteen_image));

		if (position == 0) {

			Log.i(TAG, "Load canteen element.");

			if (convertView == null)
				convertView = LayoutInflater.from(context).inflate(
						R.layout.element_canteen, null);

			((TextView) convertView.findViewById(R.id.canteen_name))
					.setText(this.canteen.getName());
			((TextView) convertView.findViewById(R.id.canteen_campus))
					.setText(this.canteen.getCampus());
			((TextView) convertView.findViewById(R.id.canteen_address))
					.setText(this.canteen.getAddress());

		} else {

			Log.i(TAG, "Load canteen details element.");

			if (convertView == null)
				convertView = LayoutInflater.from(context).inflate(
						R.layout.details_canteen, null);

			((TextView) convertView.findViewById(R.id.canteen_name_detail))
					.setText(canteen.getName());
			((TextView) convertView.findViewById(R.id.canteen_campus_detail))
					.setText(canteen.getCampus());
			((TextView) convertView.findViewById(R.id.canteen_address_detail))
					.setText(canteen.getAddress());
			((TextView) convertView.findViewById(R.id.canteen_amperiod))
					.setText(canteen.getAmPeriod());
			((TextView) convertView.findViewById(R.id.canteen_pmperiod))
					.setText(canteen.getPmPeriod());
			((TextView) convertView.findViewById(R.id.canteen_latitude))
					.setText(String.valueOf(canteen.getLatitude()));
			((TextView) convertView.findViewById(R.id.canteen_longitude))
					.setText(String.valueOf(canteen.getLongitude()));

			((ImageButton) convertView.findViewById(R.id.imageButton1))
					.setTag(canteen);
		}

		if (position == 0) {

			ImageView imageView = (ImageView) convertView
					.findViewById(R.id.canteen_image);

			FrameLayout frameLayout = ((FrameLayout) convertView
					.findViewById(R.id.canteen_loading));
			imageView.setImageBitmap(null);
			// Loading
			// image

			if (this.canteen.getPhoto().equals("null")) {
				imageView.setImageResource(R.drawable.cipl_background);
				frameLayout.setVisibility(View.GONE);
			} else if (this.imageCache.containsElement(this.canteen.getPhoto())) {
				imageView.setImageBitmap(this.imageCache
						.getElement(this.canteen.getPhoto()));
				frameLayout.setVisibility(View.GONE);
			} else
				this.addTaskSafely(new DownloadTask(this.canteen.getPhoto(),
						imageView, frameLayout));
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
					downloadList.addDownloadTask(downloadTask);
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
				downloadList.removeDownloadTaskByImageView(image);
			}
		}).start();

	}

	// [ENDREGION] Methods

}
