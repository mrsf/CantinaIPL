package pt.ipleiria.sas.mobile.cantinaipl.thread;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import pt.ipleiria.sas.mobile.cantinaipl.controller.SynchronizedDownloadList;
import pt.ipleiria.sas.mobile.cantinaipl.controller.DownloadTask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class ImageDownloader extends
		AsyncTask<SynchronizedDownloadList, DownloadTask, Void> {

	private SynchronizedDownloadList downloadList;

	
	private boolean finished=false;

	public boolean isFinished() {
		return getStatus()==Status.FINISHED;
	}

	@Override
	protected Void doInBackground(SynchronizedDownloadList... params) {
		downloadList = params[0];
		while (!downloadList.isEmpty()) {
			try {
				DownloadTask task = downloadList.getDownloadTask();
				String imageUrl = task.getUrl();
				Bitmap imageBitmap = ImageDownload(imageUrl);
				task.setImage(imageBitmap);
				publishProgress(task);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		finished=true;
		
		return null;
	}

	@Override
	protected void onProgressUpdate(DownloadTask ... values) {
		super.onProgressUpdate(values);
		for (DownloadTask t:values)
			t.getImageView().setImageBitmap(t.getImage());
	}

	/*
	 * @Override public void run() { super.run(); while (true) { try { imageView
	 * = imageViewBuffer.getImageView(); imageUrl =
	 * imageView.getContentDescription().toString(); imageBitmap =
	 * ImageDownload(imageUrl); imageView.setImageBitmap(imageBitmap); } catch
	 * (InterruptedException e) { e.printStackTrace(); } imageView = null; } }
	 */

	/*
	 * @Override protected Void doInBackground(Void... params) { while (true) {
	 * try { imageView = threadImageViewBuffer.getImageView();
	 * publishProgress(ImageDownload(imageView.getContentDescription()
	 * .toString())); } catch (InterruptedException e) { e.printStackTrace(); }
	 * } }
	 * 
	 * @Override protected void onProgressUpdate(Bitmap... values) {
	 * super.onProgressUpdate(values); imageView.setImageBitmap(values[0]); }
	 */

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
		// this.getImagesHashMap().put(imageUrl, bitmap);
		return bitmap;
	}

	/**
	 * Private method to create a cache directory of images (if not exists).
	 * 
	 * @return The reference of the cache directory.
	 */
	/*
	 * private File CreateCacheDir() { String filePath =
	 * Environment.getDataDirectory() +
	 * "/data/pt.ipleiria.sas.mobile.cantinaipl/Images/";
	 * 
	 * File cacheDir = new File(filePath); if (!cacheDir.exists())
	 * cacheDir.mkdirs();
	 * 
	 * return cacheDir; }
	 * 
	 * private void CreateImage(File dir, Bitmap result, String fileName) {
	 * 
	 * try { File file = new File(dir, fileName + ".png"); FileOutputStream fOut
	 * = new FileOutputStream(file);
	 * 
	 * result.compress(Bitmap.CompressFormat.PNG, 50, fOut); fOut.flush();
	 * fOut.close(); // canteensListAdapter.notifyDataSetChanged(); } catch
	 * (IOException ioe) { ioe.printStackTrace(); } }
	 */

	// [ENDREGION] Methods

	/*
	 * @Override protected void onPostExecute(ImageView result) {
	 * super.onPostExecute(result); String[] split; imageView = result; //File
	 * dir = CreateCacheDir(); // do {
	 * 
	 * split = imageView.getContentDescription().toString().split("/|\\.");
	 * imageName = split[split.length - 2]; //CreateImage(dir, bitmap,
	 * imageName); // data = false; /* synchronized (imageView) { if (!data) {
	 * try { imageView.wait(); } catch (InterruptedException e) {
	 * e.printStackTrace(); } } }
	 * 
	 * // } while (data); /* try { finalize(); } catch (Throwable e) {
	 * e.printStackTrace(); }
	 * 
	 * }
	 * 
	 * /*
	 * 
	 * @Override protected void finalize() throws Throwable { super.finalize();
	 * }
	 */

}
