package pt.ipleiria.sas.mobile.cantinaipl;

import java.util.LinkedList;

import pt.ipleiria.sas.mobile.cantinaipl.controller.ReserveListAdapter;
import pt.ipleiria.sas.mobile.cantinaipl.controller.UserSingleton;
import pt.ipleiria.sas.mobile.cantinaipl.model.Reserve;
import pt.ipleiria.sas.mobile.cantinaipl.task.CancellingReserve;
import pt.ipleiria.sas.mobile.cantinaipl.task.ReservesLoading;

import com.actionbarsherlock.view.MenuInflater;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * @author Márcio Francisco and Mário Correia
 * @version 2013.0701
 * @since 1.0
 * 
 */
public class ReservesActivity extends BaseActivity implements
		OnItemClickListener {

	// [REGION] Constants

	private static final String TAG = "RESERVES_ACTIVITY";
	private static final String SERVICE_PARAMS = UserSingleton.getInstance()
			.getUser().getLogin()
			+ "$" + "ipl.cantina.1213@gmail.com";

	// [ENDREGION] Constants

	private ReservesLoading reservesLoading;
	private ReserveListAdapter reserveListAdapter;
	// private LinkedList<Reserva> reserveList;
	private LinkedList<Reserve> reserveList;
	private View view;
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reserves);

		// this.reserveList = new LinkedList<Reserva>();
		this.reserveList = new LinkedList<Reserve>();
		this.reserveListAdapter = new ReserveListAdapter(this,
				R.layout.item_reserve, this.reserveList);

		ListView reservaListView = (ListView) findViewById(R.id.listViewReservas);
		reservaListView.setAdapter(reserveListAdapter);
		reservaListView.setOnItemClickListener(this);

		if (this.reservesLoading == null) {
			Log.i(TAG, "Thread to load reserves data is running.");
			this.reservesLoading = new ReservesLoading(this, this.reserveList,
					this.reserveListAdapter);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				this.reservesLoading.executeOnExecutor(super.getExec(),
						SERVICE_PARAMS);
			else
				this.reservesLoading.execute(SERVICE_PARAMS);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.options_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 0:
			return new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle("Anular reserva ?")
					.setMessage(
							"Ao anular a reserva, será reposto o valor à sua conta.")
					.setPositiveButton("Anular",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

									// Reserva itemToRemove = (Reserva) view
									// .getTag();
									Reserve itemToRemove = (Reserve) view
											.getTag();

									// -- Deleted from database ---
									if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
										new CancellingReserve(
												getApplicationContext(),
												showProgressDialog(),
												reserveListAdapter,
												itemToRemove)
												.executeOnExecutor(
														getExec(),
														UserSingleton
																.getInstance()
																.getUser()
																.getLogin()
																+ ",ipl.cantina.1213@gmail.com,"
																+ itemToRemove
																		.getId());
									else
										new CancellingReserve(
												getApplicationContext(),
												showProgressDialog(),
												reserveListAdapter,
												itemToRemove)
												.execute(UserSingleton
														.getInstance()
														.getUser().getLogin()
														+ ",ipl.cantina.1213@gmail.com,"
														+ itemToRemove.getId());

									/*
									 * DBAdapter db = new DBAdapter(view
									 * .getContext()); db.open(); if
									 * (db.deleteReserva(Integer
									 * .valueOf(itemToRemove .getIdRefeicao())))
									 * {
									 * DisplayToast("Reserva anulada com sucesso"
									 * ); } db.close();
									 */
								}
							})
					.setNegativeButton("Cancelar",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									DisplayToast("Anulação sem sucesso");
								}
							}).create();

		case 1:
			return new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle("Anular todas reserva ?")
					.setMessage(
							"Ao anular as reserva, será reposto o valor à sua conta.")
					.setPositiveButton("Anular",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

									reserveListAdapter.clear();

									// -- Deleted from database ---
									/*
									 * DBAdapter db = new DBAdapter(view
									 * .getContext()); db.open(); if
									 * (db.deleteAllReserves())
									 * DisplayToast("Todas as reservas anuladas"
									 * ); db.close();
									 */
								}
							})
					.setNegativeButton("Cancelar",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									DisplayToast("Anulação sem sucesso");
								}
							}).create();
		}
		return null;
	}

	private ProgressDialog showProgressDialog() {

		this.progressDialog = ProgressDialog.show(this, "A anular reserva",
				"Aguarde...", true, true, new OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						DisplayToast("TESTE");
						dialog.dismiss();
					}
				});

		return this.progressDialog;
	}

	@SuppressWarnings("deprecation")
	public void removeReservaOnClickHandler(View view) {
		this.view = view;
		showDialog(0);
	}

	@SuppressWarnings("deprecation")
	public void onClickDeleteAllReserves(View view) {
		this.view = view;
		showDialog(1);
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position,
			long id) {

		// Reserva reserva = ((Reserva) adapter.getAdapter().getItem(position));
		Reserve reserva = ((Reserve) adapter.getAdapter().getItem(position));

		// Reserve reserve = new Reserve();
		// reserve.setId(Integer.parseInt(reserva.getIdRefeicao()));
		// reserve.setPurchaseDate(reserva.getDataCompra());
		// reserve.setUseDate(reserva.getDataUso());
		// reserve.setPrice(reserva.getPrecoReserva());
		// reserve.setValid(true);
		// reserve.setUserLogin(UserSingleton.getInstance().getUser().getLogin());
		// reserve.setMealId(Integer.parseInt(reserva.getIdRefeicao()));
		// reserve.setAccounted(true);

		Intent intent = new Intent(this, ReserveDetailsActivity.class);
		// intent.putExtra("reserve", reserve);
		intent.putExtra("reserve", reserva);

		startActivity(intent);
	}

}
