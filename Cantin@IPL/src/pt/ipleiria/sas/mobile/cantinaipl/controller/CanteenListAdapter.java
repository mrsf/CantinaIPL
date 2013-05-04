package pt.ipleiria.sas.mobile.cantinaipl.controller;

import java.util.List;

import pt.ipleiria.sas.mobile.cantinaipl.R;
import pt.ipleiria.sas.mobile.cantinaipl.model.Canteen;
import pt.ipleiria.sas.mobile.cantinaipl.thread.ImageDownloader;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Class to custom the canteen list.
 * 
 * @author Márcio Francisco and Mário Correia
 * @version 2013.0430
 * @since 1.0
 * 
 */
public class CanteenListAdapter extends BaseAdapter {

	// [REGION] Fields

	private Context context;
	private List<Canteen> canteenList;
	//private ImagesManager imagesManager;
	private ImageDownloader downloader;
	private SynchronizedDownloadList downloadList;

	// [ENDREGION] Fields

	// [REGION] Constructors

	public CanteenListAdapter(Context context, List<Canteen> canteensList) {
		super();
		this.context = context;
		this.canteenList = canteensList;
		downloader=downloader=new ImageDownloader();
		downloadList=new SynchronizedDownloadList();
		//this.imagesManager = new ImagesManager();
	}

	// [ENDREGION] Constructors

	// [REGION] Inherited

	@Override
	public int getCount() {
		return canteenList.size();
	}

	@Override
	public Canteen getItem(int position) {
		return canteenList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Canteen canteen = canteenList.get(position);
		
		if (convertView!=null) {
			downloadList.removeDownloadTaskByImageView((ImageView)convertView.findViewById(R.id.imageViewCantina));
		}
		
		if (convertView==null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.item_cantinas, null);
		}
		
		
		((TextView) convertView.findViewById(R.id.textViewNome))
				.setText(canteen.getName());
		((TextView) convertView.findViewById(R.id.textViewCampus))
				.setText(canteen.getCampus());
		((TextView) convertView.findViewById(R.id.textViewAddress))
				.setText(canteen.getAddress());

		ImageView imageView = (ImageView) convertView
				.findViewById(R.id.imageViewCantina);
		
		// Verificar se existe na cache
		// se existir
		  // Vai buscar
		// caso contrário
		{
			imageView.setImageResource(R.drawable.food_espetadas); // Loading image
			
			try {
				downloadList.addDownloadTask(new DownloadTask(canteen.getPhotoUrl(), imageView));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (downloader.isFinished()) {
				
				downloader.execute(downloadList);
			}
		}
		
		

		/*if (!imagesManager.getImagesHashMap()
				.containsKey(canteen.getPhotoUrl())) {
			imagesManager.getImagesList().add(canteen.getPhotoUrl());
			try {
				imagesManager.getImageViewBuffer().putImageView(imageView);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			imageView.setImageBitmap(imagesManager.getImagesHashMap().get(
					canteen.getPhotoUrl()));
		}*/

		return convertView;
	}

	// [ENDREGION] Inherited

	// [REGION] Properties

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public List<Canteen> getCanteenList() {
		return canteenList;
	}

	public void setCanteenList(List<Canteen> canteenList) {
		this.canteenList = canteenList;
	}
/*
	public ImagesManager getImagesManager() {
		return imagesManager;
	}

	public void setImagesManager(ImagesManager imagesManager) {
		this.imagesManager = imagesManager;
	}
*/
	// [ENDREGION] Properties

}