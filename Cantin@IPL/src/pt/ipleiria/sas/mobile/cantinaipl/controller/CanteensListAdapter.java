package pt.ipleiria.sas.mobile.cantinaipl.controller;

import java.util.List;

import pt.ipleiria.sas.mobile.cantinaipl.R;
import pt.ipleiria.sas.mobile.cantinaipl.model.Canteen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CanteensListAdapter extends BaseAdapter {

	// [REGION] Fields

	private Context context;
	private List<Canteen> canteens;

	// [ENDREGION] Fields

	// [REGION] Constructors

	public CanteensListAdapter(Context context, List<Canteen> canteens) {
		super();
		this.context = context;
		this.canteens = canteens;
	}

	// [ENDREGION] Constructors

	// [REGION] Inherited

	@Override
	public int getCount() {
		return canteens.size();
	}

	@Override
	public Canteen getItem(int position) {
		return canteens.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Canteen canteen = canteens.get(position);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(R.layout.item_cantinas, null);

		((TextView) convertView.findViewById(R.id.textViewNome))
				.setText(canteen.getName());
		((TextView) convertView.findViewById(R.id.textViewCampus))
				.setText(canteen.getCampus());
		((ImageView) convertView.findViewById(R.id.imageViewCantina))
				.setImageResource(canteen.getPhoto());

		return convertView;
	}

	// [ENDREGION] Inherited

	// [REGION] Properties

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public List<Canteen> getCanteens() {
		return canteens;
	}

	public void setCantinas(List<Canteen> canteens) {
		this.canteens = canteens;
	}

	// [ENDREGION] Properties

}
