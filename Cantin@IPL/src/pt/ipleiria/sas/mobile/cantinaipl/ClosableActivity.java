package pt.ipleiria.sas.mobile.cantinaipl;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Abstract Class
 * 
 * This class allows closable option on activity back pressed.
 * 
 * @author Márcio Francisco and Mário Correia
 * @version 2013.0618
 * @since 1.0
 * 
 */
@SuppressWarnings("deprecation")
abstract class ClosableActivity extends BaseActivity {

	CharSequence msg = "Tem a certeza que deseja sair da Cantin@IPL?";
	CharSequence[] items = { "Iniciar a sessão quando a Cantin@IPL inicia" };
	boolean[] checkedItems = new boolean[items.length];

	// [REGION] Inherited_Methods

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { return
	 * super.onCreateOptionsMenu(menu); }
	 */

	@Override
	public void onBackPressed() {
		showDialog(0);
	}

	private LinearLayout getDialogLayout() {

		TextView tv = new TextView(this);
		tv.setPadding(10, 0, 0, 10);
		tv.setText("Tem a certeza que deseja sair da Cantin@IPL?");
		tv.setTextColor(Color.DKGRAY);
		tv.setTextSize(16);

		CheckBox cb = new CheckBox(this);
		cb.setText("Iniciar a sessão quando a Cantin@IPL inicia");
		cb.setTextColor(Color.DKGRAY);
		cb.setTextSize(14);
		cb.setChecked(true);

		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setPadding(16, 16, 16, 16);
		ll.setBackgroundColor(Color.WHITE);
		ll.addView(tv, 0);
		ll.addView(cb, 1);

		return ll;
	}

	private LinearLayout getNetworkDialogLayout() {

		TextView tv = new TextView(this);
		tv.setText("Verifique a conexão de internet?");
		tv.setTextColor(Color.DKGRAY);
		tv.setTextSize(16);
		tv.setGravity(Gravity.CENTER_HORIZONTAL);

		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setPadding(16, 16, 16, 16);
		ll.setBackgroundColor(Color.WHITE);
		ll.addView(tv, 0);

		return ll;
	}

	@Override
	protected Dialog onCreateDialog(int id) {

		switch (id) {
		case 0:
			return new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle("Sair da aplicação")
					.setView(getDialogLayout())
					.setPositiveButton("Sim",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									finish();
									System.exit(0);
								}
							})
					.setNegativeButton("Não",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									Toast.makeText(getBaseContext(),
											"Aplicação não terminada.",
											Toast.LENGTH_SHORT).show();
								}
							}).create();
		case 5:
			return new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle("Conectar à internet")
					.setView(getNetworkDialogLayout())
					.setPositiveButton("Configurações",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									Toast.makeText(getApplicationContext(),
											"Configurações", Toast.LENGTH_SHORT)
											.show();

									final Intent intent = new Intent(
											Intent.ACTION_MAIN, null);
									intent.addCategory(Intent.CATEGORY_LAUNCHER);
									final ComponentName cn = new ComponentName(
											"com.android.settings",
											"com.android.settings.wifi.WifiSettings");
									intent.setComponent(cn);
									intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									startActivity(intent);
								}
							})
					.setNegativeButton("Ignorar",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									Toast.makeText(getApplicationContext(),
											"Mensagem ignorada",
											Toast.LENGTH_SHORT).show();
								}
							}).create();
		}
		return null;

	}

	// [ENDREGION] Inherited_Methods

}
