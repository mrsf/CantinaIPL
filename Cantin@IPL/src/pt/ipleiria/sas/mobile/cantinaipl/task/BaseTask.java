package pt.ipleiria.sas.mobile.cantinaipl.task;

import android.content.Context;
import android.os.AsyncTask;

/**
 * @author Márcio Francisco and Mário Correia
 * @version 2013.0701
 * @since 1.0
 * 
 */
abstract class BaseTask<Params, Progress, Result> extends
		AsyncTask<Params, Progress, Result> {

	// [REGION] Constants

	private static final String SERVER_URL = "http://25.42.150.3/";
	private static final String SERVICE_URL = SERVER_URL
			+ "CantinaIplService.svc/";
	private static final String SERVICE_APP_PASSWORD = "ipl.cantina.1213@gmail.com";

	// [ENDREGION] Constants

	// [REGION] Variables

	private final Context context;

	// [ENDREGION] Variables

	// [REGION] Constructors

	public BaseTask(final Context context) {
		super();
		this.context = context;
	}

	// [ENDREGION] Constructors

	// [REGION] GetterAndSetters

	public static String getServerUrl() {
		return SERVER_URL;
	}

	public static String getServiceUrl() {
		return SERVICE_URL;
	}

	public Context getContext() {
		return context;
	}

	public static String getServiceAppPassword() {
		return SERVICE_APP_PASSWORD;
	}

	// [ENDREGION] GetterAndSetters

}
