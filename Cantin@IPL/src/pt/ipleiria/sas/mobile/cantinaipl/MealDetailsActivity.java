package pt.ipleiria.sas.mobile.cantinaipl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.app.ActionBar;

import pt.ipleiria.sas.mobile.cantinaipl.controller.DownloadTask;
import pt.ipleiria.sas.mobile.cantinaipl.controller.EventSingleton;
import pt.ipleiria.sas.mobile.cantinaipl.controller.ToggleButtonGroup;
import pt.ipleiria.sas.mobile.cantinaipl.controller.UserSingleton;
import pt.ipleiria.sas.mobile.cantinaipl.model.Dish;
import pt.ipleiria.sas.mobile.cantinaipl.model.Meal;
import pt.ipleiria.sas.mobile.cantinaipl.task.ImageDownloader;
import pt.ipleiria.sas.mobile.cantinaipl.task.MakingReserve;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MealDetailsActivity extends BaseActivity {

	private static final String TAG = "MEALDETAILS_ACTIVITY";

	// --- price --
	private static double precoPrato = 0.0;
	private static double precoSopa = 0.0;
	private static double precoSobremesa = 0.0;
	private static double totalGlobal = 0.0;

	private Meal meal;
	private ProgressDialog progressDialog;
	private String dishIds;

	// --- Buttons --
	ImageButton imageButton;
	ToggleButton tgPrato;
	ToggleButton tgSopa;
	ToggleButton tgSobremesa;
	ImageButton imbt;
	TextView precoTotal;

	ToggleButtonGroup botoes;

	private Context context;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meal_details);

		this.context = this;

		// disable button Comprar_senha if value = 0.00 €
		imbt = (ImageButton) findViewById(R.id.imageButtonComprarSenhaPrato);
		imbt.setEnabled(false);

		this.precoTotal = (TextView) findViewById(R.id.textPrecoTotalPrato);

		// --- Listner para o grupo dos 3 toggleButons
		ToggleButtonGroup tgGroup = new ToggleButtonGroup(this,
				R.id.toggleButtonPratoPrato, R.id.toggleButtonSopaPrato,
				R.id.toogleButtonSobremesaPrato);

		Intent intent = getIntent();
		this.meal = (Meal) intent.getParcelableExtra("meal");

		this.dishIds = "";

		final List<Dish> d = meal.getDishes();
		Collections.sort(d, new Comparator<Dish>() {
			@Override
			public int compare(Dish lhs, Dish rhs) {
				return lhs.getType().compareTo(rhs.getType());
			};
		});

		tgGroup.setGroupListener(new ToggleButtonGroup.GrupoListener() {

			@Override
			public void grupoChanged(boolean[] values) {
				double total_aux = 0.0;
				String dishIds_aux = "";
				imbt.setEnabled(false);
				if (values[0]) {
					total_aux += precoPrato;

					if (!dishIds_aux.equals(""))
						dishIds_aux += "£";

					dishIds_aux += String.valueOf(d.get(0).getId());
					imbt.setEnabled(true);
				}
				if (values[1]) {
					total_aux += precoSopa;

					if (!dishIds_aux.equals(""))
						dishIds_aux += "£";

					dishIds_aux += String.valueOf(d.get(2).getId());
					imbt.setEnabled(true);
				}
				if (values[2]) {
					total_aux += precoSobremesa;

					if (!dishIds_aux.equals(""))
						dishIds_aux += "£";

					dishIds_aux += String.valueOf(d.get(1).getId());
					imbt.setEnabled(true);
				}

				dishIds = dishIds_aux;
				totalGlobal = Math.round((total_aux) * 100.0) / 100.0;
				precoTotal.setText(formatStringToDecimal(String
						.valueOf(totalGlobal)) + " €");
			}
		});

		EventSingleton.getInstance().getEvent()
				.setDescription(this.meal.getDishes().get(1).getName());
		EventSingleton
				.getInstance()
				.getEvent()
				.setRefeicaoType(
						(this.meal.isRefeicao() ? "Almoço (" : "Jantar (")
								+ this.meal.getType() + ")");

		ActionBar ab = getSupportActionBar();
		ab.setTitle(getResources().getString(
				R.string.title_activity_meal_details)
				+ " de " + meal.getDate());

		populateView(meal);
	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
	 * menu; this adds items to the action bar if it is present.
	 * getMenuInflater().inflate(R.menu.options_menu, menu); return
	 * super.onCreateOptionsMenu(menu); }
	 */

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.options_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	// --- Metodo para o botão Conta --
	public void onClickConta(View view) {
		startActivity(new Intent(this, AccountActivity.class));
	}

	// --- Metodo para o botão Comprar_senha --
	@SuppressWarnings("deprecation")
	public void onClickComprarSenha(View view) {
		showDialog(0);
	}

	// --- Set message for dialogue box --
	@SuppressWarnings("deprecation")
	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		super.onPrepareDialog(id, dialog);
		switch (id) {
		case 0:
			TextView tv = (TextView) dialog.findViewById(android.R.id.message);
			tv.setText("Será debitado o valor de "
					+ formatStringToDecimal(String.valueOf(totalGlobal))
					+ " € da sua conta.");
		}
	}

	// --- Dialogue for purchase --
	@Override
	protected Dialog onCreateDialog(int id) {

		Toast.makeText(getBaseContext(), "valor da compra: " + totalGlobal,
				Toast.LENGTH_SHORT).show();
		switch (id) {
		case 0:
			return new AlertDialog.Builder(this)
					.setIcon(R.drawable.emo_im_money_mouth)
					.setTitle("Comprar senha ? ")
					.setMessage("")
					.setPositiveButton("Comprar",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

									if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
										new MakingReserve(context,
												showProgressDialog())
												.executeOnExecutor(
														getExec(),
														UserSingleton
																.getInstance()
																.getUser()
																.getUserName()
																+ "$ipl.cantina.1213@gmail.com$"
																+ String.valueOf(meal
																		.getId())
																+ "$"
																+ String.valueOf(
																		totalGlobal)
																		.replace(
																				'.',
																				',')
																+ "$"
																+ dishIds
																+ "$1");
									} else
										new MakingReserve(context,
												showProgressDialog())
												.execute(UserSingleton
														.getInstance()
														.getUser().getUserName()
														+ "$ipl.cantina.1213@gmail.com$"
														+ meal.getId()
														+ "$"
														+ String.valueOf(
																totalGlobal)
																.replace('.',
																		','));
								}
							})
					.setNegativeButton("Cancelar",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									Toast.makeText(getBaseContext(),
											"compra não efectuada",
											Toast.LENGTH_SHORT).show();
								}
							}).create();
		}
		return null;

	}

	private ProgressDialog showProgressDialog() {

		this.progressDialog = ProgressDialog.show(this, "A efectuar reserva",
				"Aguarde...", true, true, new OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						DisplayToast("TESTE");
						dialog.dismiss();
					}
				});

		return this.progressDialog;
	}

	private void populateView(Meal meal) {

		List<Dish> d = meal.getDishes();

		Collections.sort(d, new Comparator<Dish>() {
			@Override
			public int compare(Dish lhs, Dish rhs) {
				return lhs.getType().compareTo(rhs.getType());
			};
		});

		if (!d.get(0).getPhoto().equals("null")) {

			ImageView mainPhoto = (ImageView) findViewById(R.id.imagePratoPrato);
			FrameLayout primaryLoad = (FrameLayout) findViewById(R.id.primary_dish_load);

			if (super.getImageCache().containsElement(d.get(0).getPhoto()))
				mainPhoto.setImageBitmap(super.getImageCache().getElement(
						d.get(0).getPhoto()));
			else {

				mainPhoto.setImageBitmap(null); // Loading
												// image
				this.addTaskSafely(new DownloadTask(d.get(0).getPhoto(),
						mainPhoto, primaryLoad));

				/*
				 * final boolean taskFinished = (super.getImageDownloader() ==
				 * null ? true : super.getImageDownloader().isFinished());
				 */

				super.setRunnable(new Runnable() {
					@Override
					public void run() {
						/*
						 * if (getImageDownloader() != null && !taskFinished) {
						 * synchronized (getRunnable()) { try {
						 * getImageDownloader().cancel(true); this.wait(); }
						 * catch (Exception e) { e.printStackTrace(); } } }
						 */

						Log.i(TAG, "Thread to load 1 images.");
						setImageDownloader(new ImageDownloader(getImageCache()));

						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
							getImageDownloader().executeOnExecutor(getExec(),
									getDownloadList());
						else
							getImageDownloader().execute(getDownloadList());

					}
				});
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
					super.getExec().execute(super.getRunnable());
				else
					new Thread(super.getRunnable()).start();
			}
		}
		if (!d.get(2).getPhoto().equals("null")) {

			ImageView soupPhoto = (ImageView) findViewById(R.id.iv_search_meal_photo);
			FrameLayout secondaryLoad = (FrameLayout) findViewById(R.id.secondary_dish_load);

			if (super.getImageCache().containsElement(d.get(2).getPhoto()))
				soupPhoto.setImageBitmap(super.getImageCache().getElement(
						d.get(2).getPhoto()));
			else {

				soupPhoto.setImageBitmap(null); // Loading
												// image
				this.addTaskSafely(new DownloadTask(d.get(2).getPhoto(),
						soupPhoto, secondaryLoad));

				/*
				 * final boolean taskFinished = (super.getImageDownloader() ==
				 * null ? true : super.getImageDownloader().isFinished());
				 */

				super.setRunnable(new Runnable() {
					@Override
					public void run() {
						/*
						 * if (getImageDownloader() != null && !taskFinished) {
						 * synchronized (getRunnable()) { try {
						 * getImageDownloader().cancel(true); this.wait(); }
						 * catch (Exception e) { e.printStackTrace(); } } }
						 */

						Log.i(TAG, "Thread to load 1 images.");
						setImageDownloader(new ImageDownloader(getImageCache()));

						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
							getImageDownloader().executeOnExecutor(getExec(),
									getDownloadList());
						else
							getImageDownloader().execute(getDownloadList());

					}
				});
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
					super.getExec().execute(super.getRunnable());
				else
					new Thread(super.getRunnable()).start();
			}
			soupPhoto.setImageBitmap(null);
		}

		TextView mainName = (TextView) findViewById(R.id.labelNomePratoPrato);
		mainName.setText(d.get(0).getName());
		TextView mainDescription = (TextView) findViewById(R.id.textDescricaoPratoPrato);
		mainDescription.setText(d.get(0).getDescription());
		TextView mainPrice = (TextView) findViewById(R.id.textPrecoPratoPrato);
		String m = new String(
				formatStringToDecimal(String
						.valueOf(MealDetailsActivity.precoPrato = d.get(0)
								.getPrice())));
		mainPrice.setText(m + " €");

		TextView soupName = (TextView) findViewById(R.id.labelSopaPrato);
		soupName.setText(d.get(2).getName());
		TextView soupDescription = (TextView) findViewById(R.id.textDescricaoSopaPrato);
		soupDescription.setText(d.get(2).getDescription());
		TextView soupPrice = (TextView) findViewById(R.id.textPrecoSopaPrato);
		String s = new String(
				formatStringToDecimal(String
						.valueOf(MealDetailsActivity.precoSopa = d.get(2)
								.getPrice())));
		soupPrice.setText(s + " €");

		TextView dessertName = (TextView) findViewById(R.id.labelSobremesaPrato);
		dessertName.setText(d.get(1).getName());
		TextView dessertDescription = (TextView) findViewById(R.id.textDescricaoSobremesaPrato);
		dessertDescription.setText(d.get(1).getDescription());
		TextView dessertPrice = (TextView) findViewById(R.id.textPrecoSobremesaPrato);
		String sb = new String(
				formatStringToDecimal(String
						.valueOf(MealDetailsActivity.precoSobremesa = d.get(1)
								.getPrice())));
		dessertPrice.setText(sb + " €");

		if (!UserSingleton.getInstance().getUser().getType()) {

			((TextView) findViewById(R.id.textPrecoPratoPrato))
					.setVisibility(View.GONE);
			((TextView) findViewById(R.id.textPrecoSopaPrato))
					.setVisibility(View.GONE);
			((TextView) findViewById(R.id.textPrecoSobremesaPrato))
					.setVisibility(View.GONE);

			tgPrato = (ToggleButton) findViewById(R.id.toggleButtonPratoPrato);
			tgPrato.toggle();
			tgPrato.setVisibility(View.GONE);

			tgSopa = (ToggleButton) findViewById(R.id.toggleButtonSopaPrato);
			tgSopa.toggle();
			tgSopa.setVisibility(View.GONE);

			tgSobremesa = (ToggleButton) findViewById(R.id.toogleButtonSobremesaPrato);
			tgSobremesa.toggle();
			tgSobremesa.setVisibility(View.GONE);

			totalGlobal = meal.getPrice();

			precoTotal.setText(formatStringToDecimal(String
					.valueOf(totalGlobal)) + " €");
		} else {
			double total = precoPrato + precoSopa + precoSobremesa;
			totalGlobal = Math.round((total - (total * 0.2)) * 100.0) / 100.0;

			precoTotal.setText(formatStringToDecimal(String
					.valueOf(totalGlobal)) + " €");
		}

	}

	private void addTaskSafely(final DownloadTask downloadTask) {

		super.setRunnable(new Runnable() {
			@Override
			public void run() {
				try {
					getDownloadList().addDownloadTask(downloadTask);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			super.getExec().execute(super.getRunnable());
		else
			new Thread(super.getRunnable()).start();

	}
}
