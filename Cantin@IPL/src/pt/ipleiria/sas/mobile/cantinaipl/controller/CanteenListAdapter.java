package pt.ipleiria.sas.mobile.cantinaipl.controller;

import java.util.LinkedList;

import pt.ipleiria.sas.mobile.cantinaipl.R;
import pt.ipleiria.sas.mobile.cantinaipl.model.Canteen;
import pt.ipleiria.sas.mobile.cantinaipl.task.ImageDownloader;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;

/**
 * Class to custom the canteen list. It is a serialized class.
 * 
 * @author Márcio Francisco and Mário Correia
 * @version 2013.0609
 * @since 1.0
 * 
 */
@SuppressWarnings("deprecation")
public class CanteenListAdapter extends ListAdapter implements Parcelable {

	// [REGION] Constants

	private static final String TAG = "CANTEEN_LISTADAPTER";

	// [ENDREGION] Constants

	// [REGION] Variables

	private final LinkedList<Canteen> canteenList;

	// [ENDREGION] Variables

	// [REGION] Constructors

	public CanteenListAdapter(final Context context, ImageCache imageCache,
			ImageDownloader downloader, SynchronizedDownloadList downloadList,
			final LinkedList<Canteen> canteenList) {
		super(context, imageCache, downloadList, downloader);
		this.canteenList = canteenList;
	}

	// [ENDREGION] Constructors

	// [REGION] Inherited_Methods

	@Override
	public int getCount() {
		return this.canteenList.size();
	}

	@Override
	public Canteen getItem(int position) {
		return this.canteenList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Canteen canteen = this.canteenList.get(position);

		CanteenItemAdapter canteenItemAdapter = new CanteenItemAdapter(
				super.getContext(), canteen, super.getImageCache(),
				super.getDownloadList());

		if (convertView == null)
			convertView = LayoutInflater.from(super.getContext()).inflate(
					R.layout.item_canteen, null);

		Gallery gallery = (Gallery) convertView
				.findViewById(R.id.gallery_canteen);
		gallery.setAdapter(canteenItemAdapter);
		gallery.setOnItemClickListener((OnItemClickListener) super.getContext());

		startImageDownloader();

		return convertView;
	}

	// [ENDREGION] Inherited_Methods

	// [REGION] GetAndSet_Methods

	public LinkedList<Canteen> getCanteenList() {
		return canteenList;
	}

	// [ENDREGION] GetAndSet_Methods

	// [REGION] Parcelable_Code

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
	}

	public static final Parcelable.Creator<CanteenListAdapter> CREATOR = new Parcelable.Creator<CanteenListAdapter>() {

		@Override
		public CanteenListAdapter[] newArray(int size) {
			return new CanteenListAdapter[size];
		}

		@Override
		public CanteenListAdapter createFromParcel(Parcel source) {
			return null;
		}
	};

	// [ENDREGION] Parcelable_Code

}
