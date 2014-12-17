package pt.ipleiria.sas.mobile.cantinaipl.controller;

import android.graphics.Bitmap;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class DownloadTask {

	private String url;
	private ImageView imageView;
	private Bitmap imageToLoad;
	private FrameLayout frameLayout;

	public DownloadTask(String url, ImageView imageView, FrameLayout frameLayout) {
		this.url = url;
		this.imageView = imageView;
		this.imageToLoad = null;
		this.frameLayout = frameLayout;
	}

	public Bitmap getImage() {
		return imageToLoad;
	}

	public void setImage(Bitmap imageToLoad) {
		this.imageToLoad = imageToLoad;
	}

	public String getUrl() {
		return url;
	}

	public ImageView getImageView() {
		return imageView;
	}
	
	public FrameLayout getFrameLayout() {
		return frameLayout;
	}

}