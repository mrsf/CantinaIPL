package pt.ipleiria.sas.mobile.cantinaipl.controller;

import android.os.Build;

/**
 * 
 * @author Márcio Francisco and Mário Correia
 * @version 2013.0529
 * @since 1.0
 * 
 */
public class ImageCacheFactory {

	public static ImageCache getImageCache() {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1)
			return new ModernImageCache();
		else
			return new LegacyImageCache();

	}

}
