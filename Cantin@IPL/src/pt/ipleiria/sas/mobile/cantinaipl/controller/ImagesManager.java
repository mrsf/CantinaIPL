package pt.ipleiria.sas.mobile.cantinaipl.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Environment;
import android.util.Log;

/**
 * Class to manage the images.
 * 
 * This class is used by the BaseAdapters of the lists, to manage the image
 * downloads to a cache directory.
 * 
 * @author Márcio Francisco and Mário Correia
 * @version 2013.0430
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
	private ImageDownloadTask imageDownloadTask;

	// [ENDREGION] Fields

	// [REGION] Constructors

	public ImagesManager() {
		this.imageHashMap = new HashMap<String, Bitmap>(MAX_LENGHT);
		this.imageList = new ArrayList<String>();
		this.imageDownloadTask = new ImageDownloadTask();
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

	public ImageDownloadTask getImageDownloadTask() {
		return imageDownloadTask;
	}

	public void setImageDownloadTask(ImageDownloadTask imageDownloadTask) {
		this.imageDownloadTask = imageDownloadTask;
	}

	// [ENDREGION] Properties

	// [REGION] Methods

	private InputStream OpenHttpConnection(String urlString) throws IOException {
		InputStream in = null;
		int response = -1;

		URL url = new URL(urlString);
		URLConnection conn = url.openConnection();

		if (!(conn instanceof HttpURLConnection)) {
			throw new IOException("Not an HTTP connection");
		}
		try {
			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setAllowUserInteraction(false);
			httpConn.setInstanceFollowRedirects(true);
			httpConn.setRequestMethod("GET");
			httpConn.connect();
			response = httpConn.getResponseCode();
			if (response == HttpURLConnection.HTTP_OK) {
				in = httpConn.getInputStream();
			}
		} catch (Exception ex) {
			Log.d("Networking", ex.getLocalizedMessage());
			throw new IOException("Error connecting");
		}
		return in;
	}

	private Bitmap ImageDownload(String imageUrl) {
		Bitmap bitmap = null;
		InputStream inputStream = null;
		try {
			inputStream = OpenHttpConnection(imageUrl);
			bitmap = BitmapFactory.decodeStream(inputStream);
			inputStream.close();
		} catch (IOException e1) {
			Log.d("NetworkingActivity", e1.getLocalizedMessage());
		}
		this.getImagesHashMap().put(imageUrl, bitmap);
		return bitmap;
	}

	/**
	 * Private method to create a cache directory of images (if not exists).
	 * 
	 * @return The reference of the cache directory.
	 */
	private File CreateCacheDir() {
		String filePath = Environment.getDataDirectory()
				+ "/data/pt.ipleiria.sas.mobile.cantinaipl/Images/";

		File cacheDir = new File(filePath);
		if (!cacheDir.exists())
			cacheDir.mkdirs();

		return cacheDir;
	}

	private void CreateImage(final File dir, final Bitmap result,
			String fileName) {

		try {
			File file = new File(dir, fileName + ".png");
			FileOutputStream fOut = new FileOutputStream(file);

			result.compress(Bitmap.CompressFormat.PNG, 50, fOut);
			fOut.flush();
			fOut.close();
			// canteensListAdapter.notifyDataSetChanged();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public void NotifyThread(String... photoUrl) {
		if (imageDownloadTask.getStatus() == Status.PENDING)
			imageDownloadTask.execute(photoUrl);
	}

	// [ENDREGION] Methods

	// [REGION] Thread

	private class ImageDownloadTask extends AsyncTask<String, Void, Bitmap> {

		private String fileName;
		private int position = 0;

		protected Bitmap doInBackground(String... urls) {
			String[] split = urls[position].split("/|\\.");
			fileName = split[split.length - 2];
			return ImageDownload(urls[position]);
		}

		protected void onPostExecute(Bitmap result) {
			File dir = CreateCacheDir();
			CreateImage(dir, result, fileName);
			position++;
		}
	}

	// [ENDREGION] Thread

}