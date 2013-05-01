package pt.ipleiria.sas.mobile.cantinaipl;

import java.util.ArrayList;

import pt.ipleiria.sas.mobile.cantinaipl.controller.CanteenListAdapter;
import pt.ipleiria.sas.mobile.cantinaipl.database.CanteensRepository;
import pt.ipleiria.sas.mobile.cantinaipl.model.Canteen;
import pt.ipleiria.sas.mobile.cantinaipl.service.CanteensService;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class CanteensActivity extends Activity implements OnItemClickListener {

	// [REGION] Fields

	private ListView listView;
	private CanteenListAdapter canteensListAdapter;
	private CanteensRepository canteensRepository;
	private CanteensService canteensService;
	private ArrayList<Canteen> canteensList;

	// [ENDREGION] Fields

	// [REGION] Inherited

	public ArrayList<Canteen> getCanteensList() {
		return canteensList;
	}

	public void setCanteensList(ArrayList<Canteen> canteensList) {
		this.canteensList = canteensList;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cantinas);

		listView = (ListView) findViewById(R.id.listViewCantinas);
		listView.setOnItemClickListener(this);

		canteensRepository = new CanteensRepository(this, false);
		canteensList = new ArrayList<Canteen>();
		
		populateCanteensList();
		
		canteensService = new CanteensService(this, canteensRepository, canteensListAdapter);

		//populateCanteensList();
	}
	
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
		
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}*/


	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		Canteen canteen = canteensListAdapter.getItem(position);

		Toast.makeText(this, "Foi seleccionada a " + canteen.getName(),
				Toast.LENGTH_LONG).show();
	}

	// [ENDREGION] Inherited

	// [REGION] Methods

	public void populateCanteensList() {

		canteensList = new ArrayList<Canteen>();

		canteensRepository.open();
		canteensList = canteensRepository.GetCanteens();
		canteensRepository.close();

		canteensListAdapter = new CanteenListAdapter(this, canteensList);
		listView.setAdapter(canteensListAdapter);
		//listView.setCacheColorHint(Color.TRANSPARENT);
	}

	// [ENDREGION] Methods
	
	public CanteensService getCanteensService() {
		return canteensService;
	}

	public void setCanteensService(CanteensService canteensService) {
		this.canteensService = canteensService;
	}

}
