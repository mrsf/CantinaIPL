package pt.ipleiria.sas.mobile.cantinaipl.controller;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.util.LruCache;

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
public class ImgCache {

	private static final String CACHE_FOLDER = Environment.getDataDirectory()
			+ "/data/pt.ipleiria.sas.mobile.cantinaipl/Images/";
	private static final String TAG = "ImageCache";
	private static final int MAX_SIZE = 64;

	private LruCache<String, Bitmap> newCache;
	private LinkedHashMap<String, Bitmap> oldCache;

	// private boolean autoRemoveContext = true;

	// [REGION] Constructors
	
	public ImgCache() {
		initializeCache();
	}
	
	// [ENDREGION] Constructors
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	private void initializeCache() {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
			this.newCache = new LruCache<String, Bitmap>(MAX_SIZE) {

				@Override
				protected void entryRemoved(boolean evicted, String key,
						Bitmap oldValue, Bitmap newValue) {
					super.entryRemoved(evicted, key, oldValue, newValue);

					/*
					 * if (autoRemoveContext) { removeImage(key); Log.i(TAG,
					 * "Remove Element: " + key + "(key)"); } else { Log.i(TAG,
					 * "Move Element: " + key + "(key)"); } autoRemoveContext =
					 * true;
					 */
				}

			};
		} else {
			this.oldCache = new LinkedHashMap<String, Bitmap>(MAX_SIZE) {

				private static final long serialVersionUID = 1L;

				@Override
				protected boolean removeEldestEntry(
						java.util.Map.Entry<String, Bitmap> eldest) {
					/*
					 * if (this.size() == (MAX_SIZE + 1)) {
					 * this.remove(eldest.getKey());
					 * 
					 * if (autoRemoveContext) { removeImage(eldest.getKey());
					 * Log.i(TAG, "Remove Element: " + eldest.getKey() +
					 * "(key)"); } else { Log.i(TAG, "Move Element: " +
					 * eldest.getKey() + "(key)"); }
					 * 
					 * autoRemoveContext = true; }
					 */
					return super.removeEldestEntry(eldest);
				}

			};
		}

	}

	private void removeImage(String imageName) {

		File imageFile = new File(CACHE_FOLDER + generateChecksum(imageName));
		imageFile.delete();

	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	public synchronized void addElement(String key, Bitmap value) {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
			this.newCache.put(key, value);
			Log.i(TAG, "Add Element: " + key + "(key)");
		} else {
			this.oldCache.put(key, value);
			Log.i(TAG, "Add Element: " + key + "(key)");
		}

	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	public synchronized Bitmap getElement(String key) {

		// Bitmap value;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
			/*
			 * value = this.newCache.get(key); this.autoRemoveContext = false;
			 * this.newCache.remove(key); this.newCache.put(key, value);
			 */

			return this.newCache.get(key);
		} else {
			/*
			 * value = this.oldCache.get(key); this.autoRemoveContext = false;
			 * this.oldCache.remove(key); this.oldCache.put(key, value);
			 */

			return this.oldCache.get(key);
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	public synchronized boolean containsElement(String key) {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
			Bitmap element = this.newCache.get(key);
			return (element != null);
		} else
			return this.oldCache.containsKey(key);

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
