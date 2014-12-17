package pt.ipleiria.sas.mobile.cantinaipl;

import java.util.concurrent.ThreadPoolExecutor;

import com.actionbarsherlock.app.SherlockActivity;

import pt.ipleiria.sas.mobile.cantinaipl.controller.ImageCache;
import pt.ipleiria.sas.mobile.cantinaipl.controller.ImageCacheFactory;
import pt.ipleiria.sas.mobile.cantinaipl.controller.SynchronizedDownloadList;
import pt.ipleiria.sas.mobile.cantinaipl.parser.FormatString;
import pt.ipleiria.sas.mobile.cantinaipl.task.ImageDownloader;
import pt.ipleiria.sas.mobile.cantinaipl.task.UpdateApplication;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

/**
 * @author Márcio Francisco and Mário Correia
 * @version 2013.0609
 * @since 1.0
 * 
 */
public abstract class BaseActivity extends SherlockActivity {

	// [REGION] Constants

	public static final int N_CORES = Runtime.getRuntime()
			.availableProcessors();

	// [ENDREGION] Constants

	// [REGION] Variables

	private SynchronizedDownloadList downloadList;
	private ImageDownloader imageDownloader;
	private ImageCache imageCache;
	private Runnable runnable;
	private ProgressDialog progressDialog;

	public Runnable getRunnable() {
		return runnable;
	}

	public void setRunnable(Runnable runnable) {
		this.runnable = runnable;
	}

	private static ThreadPoolExecutor exec;

	// private static Executor oldExec;
	// private static CantinaIplApplication cantinaIplApplication;

	// [ENDREGION] Variables

	// [REGION] Inherited_Methods

	/*
	 * public static SerialTaskExecutor getOldExec() { return
	 * (SerialTaskExecutor) oldExec; }
	 * 
	 * public static void setOldExec(Executor oldExec) { BaseActivity.oldExec =
	 * oldExec; }
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/*
		 * if (BaseActivity.cantinaIplApplication == null)
		 * BaseActivity.cantinaIplApplication = (CantinaIplApplication)
		 * getApplication();
		 */

		this.downloadList = new SynchronizedDownloadList();
		this.imageDownloader = new ImageDownloader(imageCache);
		this.imageCache = ImageCacheFactory.getImageCache();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if (BaseActivity.exec == null)
				this.createThreadPool();
			/*
			 * else BaseActivity.exec.purge();
			 */
		} /*
		 * else { if (BaseActivity.oldExec == null) BaseActivity.oldExec = new
		 * SerialTaskExecutor(); }
		 */

		// exec = (ThreadPoolExecutor) Executors.newScheduledThreadPool(2);

		handleIntent(getIntent());
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void createThreadPool() {
		BaseActivity.exec = (ThreadPoolExecutor) AsyncTask.THREAD_POOL_EXECUTOR;
		BaseActivity.exec.setCorePoolSize(2);
		BaseActivity.exec.setMaximumPoolSize(16);
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			this.searchOption(searchManager, menu);
		}
		return true;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void searchOption(SearchManager searchManager,
			com.actionbarsherlock.view.Menu menu) {
		android.widget.SearchView searchView = (android.widget.SearchView) menu
				.findItem(R.id.action_search).getActionView();
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		searchView.setIconifiedByDefault(false);
	}

	// Menu Item clicked
	private boolean MenuChoice(com.actionbarsherlock.view.MenuItem item) {

		Intent i;
		switch (item.getItemId()) {
		case android.R.id.home:
			i = new Intent(getApplicationContext(), CanteensActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			return true;
		case R.id.action_reserves:
			i = new Intent(getApplicationContext(), ReservesActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			return true;
		case R.id.action_account:
			i = new Intent(getApplicationContext(), AccountActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			return true;
		case R.id.action_settings:
			i = new Intent(getApplicationContext(), SettingsActivity.class);
			startActivity(i);
			return true;
			/*
			 * case 1: finish(); System.exit(0); DisplayToast("Sair"); // kill
			 * activity return true;
			 * 
			 * case R.id.menuReservas : startActivity(new Intent(this,
			 * ReservaActivity.class));
			 * 
			 * case R.id.menu_update : new JsonUpdateApplication(this);
			 */
		case R.id.action_search:
			onSearchRequested();
			return true;
		case R.id.action_update:
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				new UpdateApplication(getApplicationContext(),
						showProgressDialog("A actualizar aplicação",
								"Actualização cancelada")).executeOnExecutor(
						BaseActivity.getExec(), "2091112$");
			else
				new UpdateApplication(getApplicationContext(),
						showProgressDialog("A actualizar aplicação",
								"Actualização cancelada")).execute("2091112$");
			return true;
		case R.id.action_about:
			DisplayToast("Projeto Informatico - Engenharia Informatica\n"
					+ "Elaborado por Márcio Francisco e Mário Correia\n"
					+ "Instituto Politecnico de Leiria");
			return true;
		}

		return false;
	}

	private ProgressDialog showProgressDialog(String title,
			final String cancelMessage) {

		this.progressDialog = ProgressDialog.show(this, title, "Aguarde...",
				true, true, new OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						DisplayToast(cancelMessage);
						dialog.dismiss();
					}
				});

		return this.progressDialog;
	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) {
	 * 
	 * if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
	 * SearchManager searchManager = (SearchManager)
	 * getSystemService(Context.SEARCH_SERVICE); SearchView searchView =
	 * (SearchView) menu.findItem( R.id.action_search).getActionView();
	 * searchView.setSearchableInfo(searchManager
	 * .getSearchableInfo(getComponentName()));
	 * searchView.setIconifiedByDefault(false); }
	 * 
	 * return true; }
	 */

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		return MenuChoice(item);
	}

	/*
	 * @Override public boolean onOptionsItemSelected(MenuItem item) { switch
	 * (item.getItemId()) { case R.id.action_search: onSearchRequested(); return
	 * true; default: return false; } }
	 */

	@Override
	protected void onNewIntent(Intent intent) {
		// Because this activity has set launchMode="singleTop", the system
		// calls this method
		// to deliver the intent if this activity is currently the foreground
		// activity when
		// invoked again (when the user executes a search from this activity, we
		// don't create
		// a new instance of this activity, so the system delivers the search
		// intent here)
		handleIntent(intent);
	}

	private void handleIntent(Intent intent) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			// handles a click on a search suggestion; launches activity to show
			// word
			String query = intent.getStringExtra(SearchManager.QUERY);
			Intent searchIntent = new Intent(this, SearchActivity.class);
			searchIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			searchIntent.putExtra("search", query);
			startActivity(searchIntent);
		}
	}

	// [ENDREGION] Inherited_Methods

	// [REGION] GetsAndSets_Methods

	public static ThreadPoolExecutor getExec() {
		return exec;
	}

	public static void setExec(ThreadPoolExecutor exec) {
		BaseActivity.exec = exec;
	}

	public SynchronizedDownloadList getDownloadList() {
		return downloadList;
	}

	public void setDownloadList(SynchronizedDownloadList downloadList) {
		this.downloadList = downloadList;
	}

	public ImageDownloader getImageDownloader() {
		return imageDownloader;
	}

	public void setImageDownloader(ImageDownloader imageDownloader) {
		this.imageDownloader = imageDownloader;
	}

	public ImageCache getImageCache() {
		return imageCache;
	}

	public void setImageCache(ImageCache imageCache) {
		this.imageCache = imageCache;
	}

	/*
	 * public static CantinaIplApplication getCantinaIplApplication() { return
	 * cantinaIplApplication; }
	 * 
	 * public static void setCantinaIplApplication( CantinaIplApplication
	 * cantinaIplApplication) { BaseActivity.cantinaIplApplication =
	 * cantinaIplApplication; }
	 */

	// [ENDREGION] GetsAndSets_Methods

	// [REGION] Methods

	// text with two decimal places format
	public String formatStringToDecimal(String val) {
		return new FormatString().stringToDecimal(val);
	}

	// Display Toast Messages
	public void DisplayToast(String msg) {
		Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
	}

	// String modify dot to comma
	public String dotToComma(String str) {
		return new FormatString().dotToComma(str);
	}

	// [ENDREGION] Methods

}
