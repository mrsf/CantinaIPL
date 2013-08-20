package pt.ipleiria.sas.mobile.cantinaipl.controller;

import java.io.File;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

/**
 * Class to manage the images.
 * 
 * This class is used by the BaseAdapter of the lists with images, to manage the
 * image downloads to a cache directory.
 * 
 * @author Márcio Francisco and Mário Correia
 * @version 2013.0529
 * @since 1.0
 * 
 */
public abstract class ImageCache {

	public static final String CACHE_FOLDER = Environment.getDataDirectory()
			+ "/data/pt.ipleiria.sas.mobile.cantinaipl/Images/";
	public static final String TAG = "IMAGE_CACHE";
	public static final int MAX_SIZE = 64;

	public ImageCache() {
		this.initializeCache();
	}

	public abstract void initializeCache();

	public abstract void addElement(String key, Bitmap value);

	public abstract Bitmap getElement(String key);

	public abstract boolean containsElement(String key);

	public void removeImage(String imageName) {

		File imageFile = new File(CACHE_FOLDER + generateChecksum(imageName));
		imageFile.delete();

	}

	public String generateChecksum(String value) {
		Checksum checksum = new CRC32();
		byte[] byteArray = value.getBytes();

		checksum.update(byteArray, 0, byteArray.length);
		long checksumValue = checksum.getValue();

		String s = String.valueOf(checksumValue);
		Log.i(TAG, "Image Name Checksum is " + s);

		return s;
	}

}
