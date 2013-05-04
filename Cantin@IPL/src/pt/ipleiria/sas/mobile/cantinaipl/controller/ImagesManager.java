package pt.ipleiria.sas.mobile.cantinaipl.controller;

import java.util.ArrayList;
import java.util.HashMap;

import pt.ipleiria.sas.mobile.cantinaipl.thread.ImageDownloader;

import android.graphics.Bitmap;

/**
 * Class to manage the images.
 * 
 * This class is used by the BaseAdapters of the lists, to manage the image
 * downloads to a cache directory.
 * 
 * @author Márcio Francisco and Mário Correia
 * @version 2013.0502
 * @since 1.0
 * 
 */
public class ImagesManager {

	// [REGION] Constants

	private static final int MAX_LENGHT = 64;

	// [ENDREGION] Constants

	// [REGION] Fields

	private HashMap<String, Bitmap> imageHashMap;
	private ArrayList<String> imageList;
	private ImageDownloader imageViewConsumer;
	private SynchronizedDownloadList imageViewBuffer;

	// [ENDREGION] Fields

	// [REGION] Constructors

	public ImagesManager() {
		this.imageHashMap = new HashMap<String, Bitmap>(MAX_LENGHT);
		this.imageList = new ArrayList<String>();
		this.imageViewBuffer = new SynchronizedDownloadList();
		this.imageViewConsumer = new ImageDownloader();
		this.imageViewConsumer.execute(this.imageViewBuffer);
		
	}

	// [ENDREGION] Constructors

	// [REGION] Properties

	public HashMap<String, Bitmap> getImagesHashMap() {
		return imageHashMap;
	}

	public void setImagesHashMap(HashMap<String, Bitmap> imagesHashMap) {
		this.imageHashMap = imagesHashMap;
	}

	public ArrayList<String> getImagesList() {
		return imageList;
	}

	public void setImagesList(ArrayList<String> imagesList) {
		this.imageList = imagesList;
	}

	public ImageDownloader getImageViewConsumer() {
		return imageViewConsumer;
	}

	public void setImageViewConsumer(ImageDownloader imageViewConsumer) {
		this.imageViewConsumer = imageViewConsumer;
	}

	public SynchronizedDownloadList getImageViewBuffer() {
		return imageViewBuffer;
	}

	public void setImageViewBuffer(SynchronizedDownloadList imageViewBuffer) {
		this.imageViewBuffer = imageViewBuffer;
	}

	// [ENDREGION] Properties

}