package pt.ipleiria.sas.mobile.cantinaipl;

import com.actionbarsherlock.view.MenuInflater;

import pt.ipleiria.sas.mobile.cantinaipl.controller.AccountSingleton;
import pt.ipleiria.sas.mobile.cantinaipl.controller.GrupoRadios;
import pt.ipleiria.sas.mobile.cantinaipl.controller.Mail;
import pt.ipleiria.sas.mobile.cantinaipl.controller.UserSingleton;
import pt.ipleiria.sas.mobile.cantinaipl.model.User;
import pt.ipleiria.sas.mobile.cantinaipl.task.AccountLoading;
import pt.ipleiria.sas.mobile.cantinaipl.task.GenerateReference;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AccountActivity extends BaseActivity {

	private static final String FUNCIONARIO = "Funcionário";
	private static final String ESTUDANTE = "Estudante";
	private static final String SERVICE_PARAMS = UserSingleton.getInstance()
			.getUser().getUserName()
			+ "$ipl.cantina.1213@gmail.com";

	private double montante;
	private ProgressDialog progressDialog;

	private TextView userNumber;
	private TextView userName;
	private TextView userCourse;
	private TextView userType;
	private TextView userMail;
	private TextView userNif;
	private GrupoRadios grupo;
	private EditText valor;

	private TextView balance;

	private Context context;
	
	private AccountLoading accountLoading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account);

		this.context = this;

		User user = UserSingleton.getInstance().getUser();
		String userType = (user.getType() ? FUNCIONARIO : ESTUDANTE);

		this.userNumber = (TextView) findViewById(R.id.textNumeroConta);
		this.userNumber.setText(user.getUserName());
		this.userName = (TextView) findViewById(R.id.textNomeConta);
		this.userName.setText(user.getName());
		this.userCourse = (TextView) findViewById(R.id.textAreaConta);
		this.userCourse.setText(user.getCourse());
		this.userType = (TextView) findViewById(R.id.textTipoConta);
		this.userType.setText(userType);
		this.userMail = (TextView) findViewById(R.id.textEmailConta);
		this.userMail.setText(user.getEmail());
		this.userNif = (TextView) findViewById(R.id.textNifConta);
		this.userNif.setText(String.valueOf(user.getNif()));

		this.valor = (EditText) findViewById(R.id.editTextOutroConta);
		this.valor.setFocusableInTouchMode(false);
		this.valor.setHint("outro");

		grupo = new GrupoRadios(this, R.id.radioButton_5, R.id.radioButton_10,
				R.id.radioButton_20, R.id.radioButton_50, R.id.radioButton_100,
				R.id.radioButtonOutroConta);

		this.balance = (TextView) findViewById(R.id.textSaldoConta);

		this.accountLoading = new AccountLoading(this, this.balance);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			this.accountLoading.executeOnExecutor(
					super.getExec(), SERVICE_PARAMS);
		else
			this.accountLoading.execute(SERVICE_PARAMS);

		// -- ImageButonMB --
		ImageButton btnMB = (ImageButton) findViewById(R.id.imageButtonMbConta);
		btnMB.setOnClickListener(new View.OnClickListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				switch (grupo.getCheckedRadio()) {
				case R.id.radioButton_5:
					montante = 5;
					break;
				case R.id.radioButton_10:
					montante = 10;
					break;
				case R.id.radioButton_20:
					montante = 20;
					break;
				case R.id.radioButton_50:
					montante = 50;
					break;
				case R.id.radioButton_100:
					montante = 100;
					break;
				case R.id.radioButtonOutroConta:
					if (valor.getText() == null
							|| valor.getText().length() < 1
							|| Double.parseDouble(valor.getText().toString()) < 5)
						DisplayToast("Valor minimo é de 5€.");
					else if (Double.parseDouble(valor.getText().toString()) > 200)
						DisplayToast("Valor máximo é de 200€.");
					else
						montante = Double.parseDouble(valor.getText()
								.toString());
					break;
				}

				if (montante < 5 || montante > 200) {
					DisplayToast("Valor minimo é de 5€.");
					montante = 0;
				} else {
					// --- Create MbRef --
					showDialog(6);
				}
				// DisplayToast(String.valueOf(montante));
			}
		});

		Button btnDevConta = (Button) findViewById(R.id.buttonDevolucaoConta);
		btnDevConta.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final ProgressDialog pd = showProgressDialog("A enviar email",
						"Envio de email cancelado");
				new Runnable() {
					@Override
					public void run() {
						sendEmail();
						pd.dismiss();
					}
				}.run();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.options_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	private LinearLayout getMbDialogLayout() {

		TextView tv = new TextView(this);
		tv.setText("Deseja pedir uma referência MB?");
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
		case 6:
			return new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle("Referência MB")
					.setView(getMbDialogLayout())
					.setPositiveButton("Sim",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
										new GenerateReference(context,
												showProgressDialog(
														"A efectuar reserva",
														"Reserva cancelada"))
												.executeOnExecutor(
														getExec(),
														dotToComma(String
																.valueOf(montante))
																+ "$"
																+ String.valueOf(AccountSingleton
																		.getInstance()
																		.getAccount()
																		.getId()));
									else
										new GenerateReference(context,
												showProgressDialog(
														"A efectuar reserva",
														"Reserva cancelada")).execute(dotToComma(String
												.valueOf(montante))
												+ "$"
												+ String.valueOf(AccountSingleton
														.getInstance()
														.getAccount().getId()));
								}
							})
					.setNegativeButton("Não",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									Toast.makeText(getApplicationContext(),
											"A referência não foi criada!",
											Toast.LENGTH_SHORT).show();
								}
							}).create();
		}
		return null;

	}

	private ProgressDialog showProgressDialog(String title,
			final String cancelMessage) {

		this.progressDialog = ProgressDialog.show(this, title, "Aguarde...",
				true, true, new OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						DisplayToast(cancelMessage);
						dialog.dismiss();
					}
				});

		return this.progressDialog;
	}

	public void sendEmail() {

		// credenciais
		String username = "ipl.cantina.1213@gmail.com";
		String password = "iplcantina1213";
		Mail m = new Mail(username, password);
		String userName = UserSingleton.getInstance().getUser().getName();
		String userEmail = UserSingleton.getInstance().getUser().getEmail();

		// corpo email
		String[] to = { "ipl.cantina.1213@gmail.com" };
		String from = userName + "(" + userEmail + ")"
				+ "<ipl.cantina.1213@gmail.com>";
		String subject = "Cantin@IPL - Pedido de devolução de dinheiro";
		String emailBody = "Cara Cantin@IPL,\n "
				+ "Eu "
				+ userName
				+ ", utilizador da vossa aplicação android, \n"
				+ "vou deixar de utilizar a aplicação, e como ainda tenho saldo \n"
				+ "disponível, nomeadamente " + balance.getText()
				+ " euros, faço o\n"
				+ " requerimento para que este me seja devolvido.\n\n"
				+ "\n\nObrigado, " + userName;

		m.setTo(to);
		m.setFrom(from);
		m.setSubject(subject);
		m.setBody(emailBody);

		try {
			if (m.send()) {
				DisplayToast("Email enviado corretamente.");
			} else {
				DisplayToast("Falha no envio do email.");
			}
		} catch (Exception e) {
			DisplayToast("Existem problemas no envio de emails.");
			Log.e("CantinaIPL", "Could not send email", e);
		}
	}

	public void onClickSenhas(View view) {
		startActivity(new Intent(this, ReservesActivity.class));
	}

	public double getMontante() {
		return montante;
	}

}
