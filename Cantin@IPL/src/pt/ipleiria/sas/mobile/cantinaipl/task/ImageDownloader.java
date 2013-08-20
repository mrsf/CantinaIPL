package pt.ipleiria.sas.mobile.cantinaipl.task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import pt.ipleiria.sas.mobile.cantinaipl.controller.ImageCache;
import pt.ipleiria.sas.mobile.cantinaipl.controller.SynchronizedDownloadList;
import pt.ipleiria.sas.mobile.cantinaipl.controller.DownloadTask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;

/**
 * Class to download images. This class allows image loading. The image can be
 * loading of local cache or, a web service on download way.
 * 
 * @author Márcio Francisco and Mário Correia
 * @version 2013.0606
 * @since 1.0
 * 
 */
public class ImageDownloader extends
		AsyncTask<SynchronizedDownloadList, DownloadTask, String> {

	// [REGION] Constants

	private static final String TAG = "IMAGE_DOWNLOADER";
	private static final String CACHE_FOLDER = (Environment
			.getExternalStorageState() == Environment.MEDIA_MOUNTED ? Environment
			.getExternalStorageDirectory().getPath().toString()
			+ "/Android/data/pt.ipleiria.sas.mobile.cantinaipl/cache/"
			: Environment.getDataDirectory().getPath().toString()
					+ "/data/pt.ipleiria.sas.mobile.cantinaipl/cache/");

	// [ENDREGION] Constants

	// [REGION] Final_Variables

	private final ImageCache imageCache;

	// [ENDREGION] Final_Variables

	// [REGION] Variables

	private SynchronizedDownloadList downloadList;
	private boolean finished;

	// [ENDREGION] Variables

	// [REGION] Constructors

	public ImageDownloader(ImageCache imageCache) {
		this.imageCache = imageCache;
		this.finished = false;
	}

	// [ENDREGION] Constructors

	// [REGION] Inherited_Methods

	public ImageCache getImageCache() {
		return imageCache;
	}

	public SynchronizedDownloadList getDownloadList() {
		return downloadList;
	}

	public void setDownloadList(SynchronizedDownloadList downloadList) {
		this.downloadList = downloadList;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	@Override
	protected String doInBackground(SynchronizedDownloadList... params) {
		this.downloadList = params[0];
		File dir = this.createDirIfNotExits();
		while (!downloadList.isEmpty()) {

			if (isCancelled())
				break;

			try {

				DownloadTask task = this.downloadList.getDownloadTask();
				String imageUrl = task.getUrl();
				String checksum = this.imageCache.generateChecksum(imageUrl);

				File imgFile = new File(CACHE_FOLDER + checksum);
				if (!imgFile.exists()) {
					Bitmap imageBitmap = this.imageDownload(imageUrl);
					task.setImage(imageBitmap);
					this.publishProgress(task);

					this.createImage(dir, imageBitmap, checksum);
				} else {
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inJustDecodeBounds = false;
					options.inSampleSize = 1;
					task.setImage(BitmapFactory.decodeFile(CACHE_FOLDER
							+ checksum, options));
					this.publishProgress(task);
				}
			} catch (InterruptedException e) {
				Log.d(TAG, e.getLocalizedMessage());
			}
		}
		this.finished = true;
		return null;
	}

	@Override
	protected void onProgressUpdate(DownloadTask... values) {
		super.onProgressUpdate(values);

		for (DownloadTask t : values) {
			t.getImageView().setImageBitmap(t.getImage());

			if (t.getFrameLayout() != null)
				t.getFrameLayout().setVisibility(View.GONE);

			this.imageCache.addElement(t.getUrl(), t.getImage());
		}
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		if (!this.downloadList.isEmpty())
			this.downloadList.clearList();
	}

	// [ENDREGION] Inherited_Methods

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
			Log.d(TAG, ex.getLocalizedMessage());
			throw new IOException("Error connecting");
		}
		return in;
	}

	private Bitmap imageDownload(String imageUrl) {
		Bitmap bitmap = null;
		InputStream inputStream = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = false;
		options.inTempStorage = new byte[32 * 1024];
		options.inSampleSize = 2;
		options.inDither = false;
		options.inPurgeable = true;
		options.inInputShareable = true;
		try {
			inputStream = OpenHttpConnection(imageUrl);
			bitmap = BitmapFactory.decodeStream(inputStream, null, options);
			inputStream.close();
		} catch (IOException e1) {
			Log.d(TAG, e1.getLocalizedMessage());
		}
		return bitmap;
	}

	/**
	 * Private method to create a cache directory of images (if not exists).
	 * 
	 * @return The reference of the cache directory.
	 */
	private File createDirIfNotExits() {
		String filePath = CACHE_FOLDER;

		File cacheDir = new File(filePath);
		if (!cacheDir.exists())
			cacheDir.mkdirs();

		return cacheDir;
	}

	private void createImage(File dir, Bitmap result, String fileName) {

		try {
			File file = new File(dir, fileName);
			FileOutputStream fOut = new FileOutputStream(file);

			result.compress(Bitmap.CompressFormat.PNG, 75, fOut);
			fOut.flush();
			fOut.close();
		} catch (IOException ioe) {
			Log.d(TAG, ioe.getLocalizedMessage());
		}
	}

	// [ENDREGION] Methods

	// [REGION] GetAndSet_Methods

	public boolean isFinished() {
		return finished;
	}

	// [ENDREGION] GetAndSet_Methods

}
