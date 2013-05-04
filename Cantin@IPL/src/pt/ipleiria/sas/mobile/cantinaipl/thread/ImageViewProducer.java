package pt.ipleiria.sas.mobile.cantinaipl.thread;

import android.widget.BaseAdapter;
import pt.ipleiria.sas.mobile.cantinaipl.controller.SynchronizedDownloadList;

public class ImageViewProducer extends Thread {

	private SynchronizedDownloadList imageViewBuffer;
	private BaseAdapter baseAdapter;

	public ImageViewProducer(SynchronizedDownloadList imageViewBuffer, BaseAdapter baseAdapter) {
		this.imageViewBuffer = imageViewBuffer;
		this.baseAdapter = baseAdapter;
		this.start();
	}

	@Override
	public void run() {
		
		super.run();
	}

}
