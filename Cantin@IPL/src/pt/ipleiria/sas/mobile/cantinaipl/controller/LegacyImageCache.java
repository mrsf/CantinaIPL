package pt.ipleiria.sas.mobile.cantinaipl.controller;

import java.util.LinkedHashMap;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * 
 * @author Márcio Francisco and Mário Correia
 * @version 2013.0529
 * @since 1.0
 * 
 */
public class LegacyImageCache extends ImageCache {

	private LinkedHashMap<String, Bitmap> imgCache;

	public LegacyImageCache() {
		super();
	}

	@Override
	public void initializeCache() {

		this.imgCache = new LinkedHashMap<String, Bitmap>(MAX_SIZE) {

			private static final long serialVersionUID = 1L;

			@Override
			protected boolean removeEldestEntry(
					java.util.Map.Entry<String, Bitmap> eldest) {
				/*
				 * if (this.size() == (MAX_SIZE + 1)) {
				 * this.remove(eldest.getKey());
				 * 
				 * if (autoRemoveContext) { removeImage(eldest.getKey());
				 * Log.i(TAG, "Remove Element: " + eldest.getKey() + "(key)"); }
				 * else { Log.i(TAG, "Move Element: " + eldest.getKey() +
				 * "(key)"); }
				 * 
				 * autoRemoveContext = true; }
				 */
				return super.removeEldestEntry(eldest);
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
		 * value = this.oldCache.get(key); this.autoRemoveContext = false;
		 * this.oldCache.remove(key); this.oldCache.put(key, value);
		 */

		return this.imgCache.get(key);
	}

	@Override
	public synchronized boolean containsElement(String key) {
		return this.imgCache.containsKey(key);
	}

}
