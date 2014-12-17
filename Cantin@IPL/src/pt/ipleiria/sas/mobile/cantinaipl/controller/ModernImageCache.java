package pt.ipleiria.sas.mobile.cantinaipl.controller;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.util.LruCache;

/**
 * 
 * @author Márcio Francisco and Mário Correia
 * @version 2013.0529
 * @since 1.0
 * 
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
public class ModernImageCache extends ImageCache {

	private LruCache<String, Bitmap> imgCache;
	
	public ModernImageCache() {
		super();
	}

	@Override
	public void initializeCache() {

		this.imgCache = new LruCache<String, Bitmap>(MAX_SIZE) {

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

	}

	@Override
	public synchronized void addElement(String key, Bitmap value) {
		this.imgCache.put(key, value);
		Log.i(TAG, "Add Element: " + key + "(key)");
	}

	@Override
	public synchronized Bitmap getElement(String key) {
		/*
		 * value = this.newCache.get(key); this.autoRemoveContext = false;
		 * this.newCache.remove(key); this.newCache.put(key, value);
		 */

		return this.imgCache.get(key);
	}

	@Override
	public synchronized boolean containsElement(String key) {
		Bitmap element = this.imgCache.get(key);
		return (element != null);
	}

}
