package pt.ipleiria.sas.mobile.cantinaipl.controller;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class DownloadTask {
	private String url;
	private ImageView imageView;
	private Bitmap imagemACarregar;
	
	public DownloadTask(String url, ImageView imageView) {
		super();
		this.url = url;
		this.imageView = imageView;
		this.imagemACarregar = null;
	}

	public Bitmap getImage() {
		return imagemACarregar;
	}

	public void setImage(Bitmap imagemACarregar) {
		this.imagemACarregar = imagemACarregar;
	}

	public String getUrl() {
		return url;
	}

	public ImageView getImageView() {
		return imageView;
	}
	
	
	
}
