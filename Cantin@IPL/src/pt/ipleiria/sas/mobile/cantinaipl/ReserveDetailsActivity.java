package pt.ipleiria.sas.mobile.cantinaipl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.actionbarsherlock.view.MenuInflater;

import pt.ipleiria.sas.mobile.cantinaipl.R;
import pt.ipleiria.sas.mobile.cantinaipl.controller.BarcodeView;
import pt.ipleiria.sas.mobile.cantinaipl.controller.UserSingleton;
import pt.ipleiria.sas.mobile.cantinaipl.model.Dish;
import pt.ipleiria.sas.mobile.cantinaipl.model.Reserve;
import pt.ipleiria.sas.mobile.cantinaipl.task.CheckingReserve;
import pt.ipleiria.sas.mobile.cantinaipl.task.SubmitRating;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;

public class ReserveDetailsActivity extends BaseActivity {

	private TextView dishType;
	private Reserve reserve;

	private double ratingValue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reserve_details);
		// AndroidBarcodeView view = new AndroidBarcodeView(this);
		// setContentView(view);

		Intent intent = getIntent();
		this.reserve = (Reserve) intent.getParcelableExtra("reserve");

		BarcodeView view = (BarcodeView) findViewById(R.id.barCode);
		view.newBarCode(String.valueOf(this.reserve.getId()) + ";"
				+ this.reserve.getPurchaseDate() + ";"
				+ this.reserve.getUseDate() + ";" + this.reserve.getPrice()
				+ ";" + this.reserve.getUserLogin() + ";"
				+ this.reserve.getMealId() + ";;");

		this.dishType = (TextView) findViewById(R.id.textView10);
		this.dishType
				.setText((this.reserve.getType().equals("carne") ? "Carne"
						: (this.reserve.getType().equals("peixe") ? "Peixe"
								: "Vegetariano"))
						+ " "
						+ (!UserSingleton.getInstance().getUser().isType() ? "(Estudante)"
								: "(Funcionário)"));

		new CheckingReserve(this).executeOnExecutor(super.getExec(),
				String.valueOf(reserve.getId()));
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
					.setIcon(android.R.drawable.star_big_on)
					.setTitle("Avaliar prato!")
					.setView(getDialogLayout())
					.setPositiveButton("Avaliar",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									new SubmitRating(getBaseContext()).execute("2091112$ipl.cantina.1213@gmail.com$2$"
											+ dotToComma(String
													.valueOf(ratingValue)));
								}
							})
					.setNegativeButton("Cancelar",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									Toast.makeText(getBaseContext(),
											"O prato não foi avaliado.",
											Toast.LENGTH_SHORT).show();
								}
							}).create();
		}
		return null;

	}

	private LinearLayout getDialogLayout() {

		List<Dish> d = this.reserve.getDishes();

		Collections.sort(d, new Comparator<Dish>() {
			@Override
			public int compare(Dish lhs, Dish rhs) {
				return lhs.getType().compareTo(rhs.getType());
			};
		});

		TextView tv = new TextView(this);
		tv.setPadding(10, 0, 0, 10);
		tv.setText(d.get(0).getDescription());
		tv.setTextColor(Color.DKGRAY);
		tv.setTextSize(16);

		View v = new View(this);
		v.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, 10));

		TextView tvv = new TextView(this);
		tvv.setPadding(10, 0, 0, 10);
		tvv.setText("Classifique:");
		tvv.setTextColor(Color.DKGRAY);
		tvv.setTextSize(12);

		RatingBar rb = new RatingBar(this);
		rb.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		rb.setNumStars(5);
		rb.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				ratingValue = rating;
			}
		});

		View vv = new View(this);
		vv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, 10));

		TextView tvvv = new TextView(this);
		tvvv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		
		tvvv.setText("A minha média é " + String.format("%.1f", d.get(0).getRating())
				+ " estrelas.");
		tvvv.setTextColor(Color.DKGRAY);
		tvvv.setTextSize(12);

		LinearLayout ll = new LinearLayout(this);
		ll.setGravity(Gravity.CENTER_HORIZONTAL);
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setPadding(4, 4, 4, 4);
		ll.setBackgroundColor(Color.WHITE);
		ll.addView(tv, 0);
		ll.addView(v, 1);
		ll.addView(tvv, 2);
		ll.addView(rb, 3);
		ll.addView(vv, 4);
		ll.addView(tvvv, 5);

		return ll;
	}

}
