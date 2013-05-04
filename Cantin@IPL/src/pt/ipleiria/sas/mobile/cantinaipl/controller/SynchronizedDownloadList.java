package pt.ipleiria.sas.mobile.cantinaipl.controller;

import java.util.LinkedList;

import android.util.Log;
import android.widget.ImageView;

public class SynchronizedDownloadList {

	private static final int MAX_LENGHT = 3;
	private LinkedList<DownloadTask> imageViewList;
	private int totalImageViews;

	public SynchronizedDownloadList() {
		this.imageViewList = new LinkedList<DownloadTask>();
		this.totalImageViews = 0;
	}

	public synchronized void addDownloadTask(DownloadTask imageView)
			throws InterruptedException {
		
		while (totalImageViews == MAX_LENGHT)
			wait();

		imageViewList.add(imageView);
		Log.i("THREADIPL", "PUT - " + imageView.getUrl());
		++totalImageViews;

		notifyAll();

	}

	public synchronized DownloadTask getDownloadTask() throws InterruptedException {

		while (totalImageViews == 0)
			wait();

		DownloadTask imageView = imageViewList.remove();
		Log.i("THREADIPL", "GET - " + imageView.getUrl());
		--totalImageViews;

		return imageView;

	}

	public synchronized boolean isEmpty() {
		// TODO Auto-generated method stub
		return imageViewList.isEmpty();
	}

	public synchronized void removeDownloadTaskByImageView(ImageView imagem) {
		int indice=0;
		
		for (indice=0; indice<imageViewList.size(); indice++)
			if (imageViewList.get(indice).getImageView()==imagem)
				break;
		
		if (indice<imageViewList.size()) { // encontrei
			imageViewList.remove(indice);
		}
	}

}
