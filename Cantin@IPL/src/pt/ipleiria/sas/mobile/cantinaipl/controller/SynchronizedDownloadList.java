package pt.ipleiria.sas.mobile.cantinaipl.controller;

import java.util.LinkedList;

import android.util.Log;
import android.widget.ImageView;

public class SynchronizedDownloadList {

	// [REGION] Constants

	private static final String TAG = "SYNCLIST";
	private static final int MAX_LENGHT = 1;

	// [ENDREGION] Constants

	// [REGION] Variables

	private LinkedList<DownloadTask> taskList;
	private int totalTasks;

	// [ENDREGION] Variables

	// [REGION] Constructors

	public SynchronizedDownloadList() {
		this.taskList = new LinkedList<DownloadTask>();
		this.totalTasks = 0;
	}

	// [ENDREGION] Constructors

	// [REGION] Synchronized_Methods

	public boolean contains(String url) {
		boolean exist = false;
		
		for (DownloadTask task : this.taskList) {
			if (url == task.getUrl()) {
				exist = true;
				break;
			}
		}

		return exist;
	}

	public void clearList() {
		taskList.clear();
		totalTasks = 0;
	}

	public synchronized void addDownloadTask(DownloadTask task)
			throws InterruptedException {

		while (totalTasks == MAX_LENGHT)
			wait();

		taskList.add(task);
		Log.i(TAG, "ADD - " + task.getUrl());
		++totalTasks;

		notify();

	}

	public synchronized DownloadTask getDownloadTask()
			throws InterruptedException {

		while (totalTasks == 0)
			wait();

		DownloadTask task = taskList.remove();
		Log.i(TAG, "GET - " + task.getUrl());
		--totalTasks;

		notify();

		return task;

	}

	public synchronized boolean isEmpty() {
		return taskList.isEmpty();
	}

	public synchronized void removeDownloadTaskByImageView(ImageView image) {
		int index = 0;

		for (index = 0; index < taskList.size(); index++)
			if (taskList.get(index).getImageView() == image)
				break;

		if (index < taskList.size()) { // encontrei
			taskList.remove(index);
		}
	}

	// [ENDREGION] Synchronized_Methods

}
