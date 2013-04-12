package pt.ipleiria.sas.mobile.cantinaipl;

import java.util.ArrayList;

import pt.ipleiria.sas.mobile.cantinaipl.controller.CanteensListAdapter;
import pt.ipleiria.sas.mobile.cantinaipl.database.CanteensRepository;
import pt.ipleiria.sas.mobile.cantinaipl.model.Canteen;
import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class CanteensActivity extends Activity implements OnItemClickListener {

	// [REGION] Fields

	private ListView listView;
	private CanteensListAdapter canteensListAdapter;
	private CanteensRepository canteensRepository;
	private ArrayList<Canteen> canteens;
	private Cursor canteensCursor;

	// [ENDREGION] Fields

	// [REGION] Inherited

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cantinas);

		listView = (ListView) findViewById(R.id.listViewCantinas);
		listView.setOnItemClickListener(this);

		canteensRepository = new CanteensRepository(this, false);
		canteensRepository.open();
		canteensRepository.populateTable();
		canteensRepository.close();

		populateCanteensList();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		Canteen canteen = canteensListAdapter.getItem(position);

		Toast.makeText(this, "Foi seleccionada a " + canteen.getName(),
				Toast.LENGTH_LONG).show();
	}

	// [ENDREGION] Inherited

	// [REGION] Methods

	private void populateCanteensList() {

		canteens = new ArrayList<Canteen>();

		canteensRepository.open();
		canteensCursor = canteensRepository.GetCanteens();
		if (canteensCursor.moveToFirst()) {
			do {
				canteens.add(new Canteen(Integer.parseInt(canteensCursor
						.getString(0)), canteensCursor.getString(1),
						canteensCursor.getString(2), canteensCursor
								.getString(3), canteensCursor.getString(4),
						canteensCursor.getString(5), Integer
								.parseInt(canteensCursor.getString(6)), Double
								.parseDouble(canteensCursor.getString(7)),
						Double.parseDouble(canteensCursor.getString(8))));
			} while (canteensCursor.moveToNext());
		}
		canteensRepository.close();

		canteensListAdapter = new CanteensListAdapter(this, canteens);
		listView.setAdapter(canteensListAdapter);
		listView.setCacheColorHint(Color.TRANSPARENT);
	}

	// [ENDREGION] Methods

}
