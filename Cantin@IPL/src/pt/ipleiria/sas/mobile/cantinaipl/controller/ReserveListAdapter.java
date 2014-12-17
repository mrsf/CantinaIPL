package pt.ipleiria.sas.mobile.cantinaipl.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pt.ipleiria.sas.mobile.cantinaipl.R;
import pt.ipleiria.sas.mobile.cantinaipl.model.Dish;
import pt.ipleiria.sas.mobile.cantinaipl.model.Reserve;
import pt.ipleiria.sas.mobile.cantinaipl.parser.FormatString;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

//public class ReserveListAdapter extends ArrayAdapter<Reserva> {
public class ReserveListAdapter extends ArrayAdapter<Reserve> {

	static final int PRATO_CARNE_PEIXE_INDEX = 0;
	static final int PRATO_SOPA_INDEX = 1;
	static final int PRATO_SOBREMESA_INDEX = 2;

	// private List<Reserva> items;
	private List<Reserve> items;
	private int layoutResourceId;
	private Context context;

	// --- Constructor ---
	// public ReserveListAdapter(Context context, int layoutResourceId,
	// List<Reserva> items) {
	public ReserveListAdapter(Context context, int layoutResourceId,
			List<Reserve> items) {
		super(context, layoutResourceId, items);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.items = items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		// ReservaHolder holder = null;
		ReserveHolder holder = null;

		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		view = inflater.inflate(layoutResourceId, parent, false);

		// holder = new ReservaHolder();
		// holder.reserva = items.get(position);
		// holder.removeReservaButton = (Button) view
		// .findViewById(R.id.buttonAnularReservaReserva);
		// holder.removeReservaButton.setTag(holder.reserva);
		holder = new ReserveHolder();
		holder.reserve = items.get(position);
		holder.removeReserveButton = (Button) view
				.findViewById(R.id.buttonAnularReservaReserva);
		holder.removeReserveButton.setTag(holder.reserve);

		// -- id reserva --
		TextView txtLabelId = (TextView) view
				.findViewById(R.id.labelIdReservas);
		// txtLabelId.setText(holder.reserva.getIdRefeicao());
		txtLabelId.setText(String.valueOf(holder.reserve.getId()));

		List<Dish> d = holder.reserve.getDishes();

		Collections.sort(d, new Comparator<Dish>() {
			@Override
			public int compare(Dish lhs, Dish rhs) {
				return lhs.getType().compareTo(rhs.getType());
			};
		});

		// -- prato carne ou peixe --
		TextView txtLabelPrato = (TextView) view
				.findViewById(R.id.labelPratoReservas);
		// txtLabelPrato.setText(holder.reserva.getPratoIndex(
		// PRATO_CARNE_PEIXE_INDEX).getName());
		txtLabelPrato.setText(d.get(0).getName());

		TextView descPrato = (TextView) view
				.findViewById(R.id.textPratoReservas);
		// descPrato.setText(holder.reserva.getPratoIndex(PRATO_CARNE_PEIXE_INDEX)
		// .getDescription());
		descPrato.setText(d.get(0).getDescription());

		// -- sopa --
		TextView txtLabelSopa = (TextView) view
				.findViewById(R.id.labelSopaReservas);
		// txtLabelSopa.setText(holder.reserva.getPratoIndex(PRATO_SOPA_INDEX)
		// .getName());
		txtLabelSopa.setText(d.get(2).getName());
		TextView descSopa = (TextView) view.findViewById(R.id.textSopaReservas);
		// descSopa.setText(holder.reserva.getPratoIndex(PRATO_SOPA_INDEX)
		// .getDescription());
		descSopa.setText(d.get(2).getDescription());

		// -- sobremesa --
		TextView txtLabelSobrem = (TextView) view
				.findViewById(R.id.labelSobremesaReservas);
		// txtLabelSobrem.setText(holder.reserva.getPratoIndex(
		// PRATO_SOBREMESA_INDEX).getName());
		txtLabelSobrem.setText(d.get(1).getName());
		TextView descSobrem = (TextView) view
				.findViewById(R.id.textSobremesaReservas);
		// descSobrem.setText(holder.reserva.getPratoIndex(PRATO_SOBREMESA_INDEX)
		// .getDescription());
		descSobrem.setText(d.get(1).getDescription());

		// -- Data da refeição --
		TextView txtDta = (TextView) view.findViewById(R.id.labelDataReservas);
		// txtDta.setText(holder.reserva.getDataUso());
		txtDta.setText(holder.reserve.getUseDate());

		// -- Refeição almoço/jantar
		TextView txtRef = (TextView) view
				.findViewById(R.id.textRefeicaoReservas);
		// txtRef.setText(holder.reserva.getRefeicao());

		/* txtRef.setText(holder.reserve.getRefeicao()); */

		// -- Preço --
		TextView txtPreco = (TextView) view
				.findViewById(R.id.textPrecoReservas);
		FormatString fs = new FormatString();
		// txtPreco.setText(fs.stringToDecimal(String.valueOf(holder.reserva
		// .getPrecoReserva())) + " €");
		txtPreco.setText(fs.stringToDecimal(String.valueOf(holder.reserve
				.getPrice())) + " €");

		// -- Cantina --
		TextView txtCantina = (TextView) view
				.findViewById(R.id.labelCantinaReservas);
		// txtCantina.setText(holder.reserva.getCantina());
		txtCantina.setText(String.valueOf(holder.reserve.getCanteenId()));

		view.setTag(holder);
		return view;
	}

	public static class ReserveHolder {
		Reserve reserve;
		Button removeReserveButton;
	}

	// public static class ReservaHolder {
	// Reserva reserva;
	// Button removeReservaButton;
	// }
}
