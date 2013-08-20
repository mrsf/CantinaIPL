package pt.ipleiria.sas.mobile.cantinaipl.controller;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import android.os.AsyncTask;

public class SerialTaskExecutor implements Executor {

	private static final int MAX = 16;

	private Executor executor;
	@SuppressWarnings("rawtypes")
	private final Queue tasksPool = new ArrayBlockingQueue(MAX);
	private Runnable active;

	public SerialTaskExecutor() {
		this.executor = (Executor) Executors.newScheduledThreadPool(1);
	}

	public synchronized void executeOnExecutor(
			@SuppressWarnings("rawtypes") final AsyncTask task,
			final Object... params) {
		this.execute(new Runnable() {
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				try {
					((AsyncTask<Object, Object, Object>) task).execute(params);
				} finally {
					scheduleNext();
				}
			}
		});
	}

	protected synchronized void scheduleNext() {
		if ((this.active = (Runnable) this.tasksPool.poll()) != null) {
			this.executor.execute(active);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized void execute(Runnable command) {
		this.tasksPool.offer(command);
		if (this.active == null) {
			this.scheduleNext();
		}
	}

}
