package pt.ipleiria.sas.mobile.cantinaipl.controller;

import pt.ipleiria.sas.mobile.cantinaipl.BaseActivity;
import pt.ipleiria.sas.mobile.cantinaipl.task.ImageDownloader;

import android.content.Context;
import android.os.Build;
import android.os.AsyncTask.Status;
import android.widget.BaseAdapter;

abstract class ListAdapter extends BaseAdapter {

	private final Context context;
	private final ImageCache imageCache;
	private final SynchronizedDownloadList downloadList;

	private static ImageDownloader downloader;
	private Runnable runnable;

	public ListAdapter(final Context context, final ImageCache imageCache,
			final SynchronizedDownloadList downloadList,
			ImageDownloader downloader) {
		super();
		this.context = context;
		this.imageCache = imageCache;
		this.downloadList = downloadList;
		ListAdapter.downloader = downloader;
	}

	public void downloadImages(Runnable runnable) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			BaseActivity.getExec().execute((this.runnable = runnable));
		else
			new Thread((this.runnable = runnable)).start();
	}

	public void startImageDownloader() {

		if (downloader.isFinished() || downloader.getStatus() == Status.PENDING) {
			// Log.i(TAG, "Image downloader task is working!");
			downloader = new ImageDownloader(imageCache);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				downloader.executeOnExecutor(BaseActivity.getExec(),
						getDownloadList());
			else
				downloader.execute(getDownloadList());

			// BaseActivity.getOldExec().executeOnExecutor((AsyncTask)
			// downloader, getDownloadList());
		}

	}

	public Context getContext() {
		return context;
	}

	public ImageCache getImageCache() {
		return imageCache;
	}

	public SynchronizedDownloadList getDownloadList() {
		return downloadList;
	}

	public static ImageDownloader getDownloader() {
		return downloader;
	}

	public static void setDownloader(ImageDownloader downloader) {
		ListAdapter.downloader = downloader;
	}

	public Runnable getRunnable() {
		return runnable;
	}

}
