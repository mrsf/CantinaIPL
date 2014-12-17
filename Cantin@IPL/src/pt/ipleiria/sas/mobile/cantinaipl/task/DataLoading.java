package pt.ipleiria.sas.mobile.cantinaipl.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * 
 * @author Márcio Francisco and Mário Correia
 * @version 2013.0612
 * @since 1.0
 * 
 */
abstract class DataLoading<Params, Progress, Result> extends
		BaseTask<Params, Progress, Result> {

	// [REGION] Constants

	private static final String TAG = "DATA_LOADING";

	// [ENDREGION] Constants

	// [REGION] Constructors

	protected DataLoading(final Context context) {
		super(context);
	}

	// [ENDREGION] Constructors

	// [REGION] Methods

	/**
	 * <b>Method to get json data</b>
	 * 
	 * <p>
	 * This method allows get the json data of a server url.
	 * </p>
	 * 
	 * @param serviceUrl
	 *            Server url to get the data.
	 * @return A string with json text.
	 */
	public String readJsonData(String serviceUrl) {

		StringBuilder stringBuilder = new StringBuilder();
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(serviceUrl);

		try {
			HttpResponse response = httpClient.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity httpEntity = response.getEntity();
				InputStream content = httpEntity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					stringBuilder.append(line);
				}
			} else {
				Log.e(TAG, "Failed to get Json data file.");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return stringBuilder.toString();
	}

	/**
	 * <b>Method to verify network state</b>
	 * 
	 * <p>
	 * This method allow verify is the network is available or not.
	 * </p>
	 * 
	 * @return true if network available otherwise false.
	 */
	public boolean isNetworkAvailable() {

		ConnectivityManager connectivityManager = (ConnectivityManager) super
				.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();

		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	// [ENDREGION] Methods

}
