package pt.ipleiria.sas.mobile.cantinaipl.task;

import org.json.JSONObject;

import pt.ipleiria.sas.mobile.cantinaipl.BaseActivity;
import pt.ipleiria.sas.mobile.cantinaipl.CantinaIplApplication;
import pt.ipleiria.sas.mobile.cantinaipl.R;
import pt.ipleiria.sas.mobile.cantinaipl.controller.Mail;
import pt.ipleiria.sas.mobile.cantinaipl.controller.UserSingleton;
import pt.ipleiria.sas.mobile.cantinaipl.database.ReferencesRepository;
import pt.ipleiria.sas.mobile.cantinaipl.model.Reference;
import pt.ipleiria.sas.mobile.cantinaipl.model.User;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class GenerateReference extends DataLoading<String, Void, String> {

	// [REGION] Constants

	private static final String TAG = "GENERATE_REFERENCE";
	private static final String SERVICE_METHOD = getServiceUrl() + "GetMbRef/"
			+ getServiceAppPassword() + "$";

	// [ENDREGION] Constants

	private ProgressDialog progressDialog;
	private User user;

	// Constructor
	public GenerateReference(final Context context,
			ProgressDialog progressDialog) {
		super(context);
		this.progressDialog = progressDialog;
		this.user = UserSingleton.getInstance().getUser();
	}

	protected String doInBackground(String... params) {
		return super.readJsonData(SERVICE_METHOD + params[0]);
	}

	protected void onPostExecute(String result) {
		try {
			if (!result.equals("")) {
				JSONObject jsonObject = new JSONObject(result);
				Log.i(TAG, "Get MB reference");

				saveRef(jsonObject.getInt("Id"),
						jsonObject.getString("Entity"),
						jsonObject.getString("Refer"),
						jsonObject.getDouble("Amount"),
						jsonObject.getInt("AccountId"),
						jsonObject.getString("EmiDate"),
						jsonObject.getString("ExpDate"),
						jsonObject.getString("Status"),
						jsonObject.getBoolean("IsPaid"));

				showRef(jsonObject.getString("Entity"),
						jsonObject.getString("Refer"),
						jsonObject.getDouble("Amount"),
						jsonObject.getString("ExpDate"));

				this.progressDialog.dismiss();

				Toast.makeText(super.getContext(),
						"A referência foi criada com sucesso.",
						Toast.LENGTH_SHORT).show();
			}
			this.progressDialog.dismiss();

			Toast.makeText(super.getContext(), "A referência não foi criada!",
					Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			e.printStackTrace();
			this.progressDialog.dismiss();

			Toast.makeText(super.getContext(), "A referência não foi criada!",
					Toast.LENGTH_SHORT).show();
		}
	}

	// --- Save MbRef generated to database --
	void saveRef(int idRef, String entity, String reference, Double amount,
			int accountId, String emitionDate, String expiratioDate,
			String status, Boolean isPaid) {
		ReferencesRepository referencesRepository = new ReferencesRepository(
				super.getContext(), false);
		referencesRepository.open();
		referencesRepository.insertReference(new Reference(idRef, entity,
				reference, amount, accountId, emitionDate, expiratioDate,
				status, isPaid));
		referencesRepository.close();
	}

	// --- Show on UI the new MbRef --
	void showRef(String entity, String reference, Double amount, String expDate) {

		Activity activity = (Activity) super.getContext();
		// --- getViews --
		TextView ent = (TextView) activity.findViewById(R.id.textEntidadeConta);
		TextView ref = (TextView) activity
				.findViewById(R.id.textReferenciaConta);
		TextView amt = (TextView) activity.findViewById(R.id.textMontanteConta);
		TextView exp = (TextView) activity.findViewById(R.id.textDataConta);
		// --- setDataToViews --
		ent.setText(entity);
		ref.setText(reference);
		amt.setText(((BaseActivity) activity).formatStringToDecimal(String
				.valueOf(amount)) + " €");
		exp.setText(expDate);

		if (((CantinaIplApplication) super.getContext().getApplicationContext())
				.isEmailsActive())
			sendEmail(entity, reference, amount, expDate);
	}

	public void sendEmail(String entity, String reference, Double amount,
			String expDate) {

		// credenciais
		String username = "ipl.cantina.1213@gmail.com";
		String password = "iplcantina1213";
		Mail m = new Mail(username, password);

		String refData = "Entidade: "
				+ entity
				+ "\nReferência: "
				+ reference
				+ "\nMontante: "
				+ ((BaseActivity) super.getContext())
						.formatStringToDecimal(String.valueOf(amount)) + "€"
				+ "\nData limite de Pagamento: " + expDate;

		// corpo email
		String[] to = { user.getEmail() };
		String from = "Cantin@IPL <ipl.cantina.1213@gmail.com>";
		String subject = "Cantin@IPL - Referência de multibanco";
		String emailBody = "Caro(a) "
				+ user.getName()
				+ ",\n\nVoçê emitiu uma referencia MB, via a nossa aplicação android, \n"
				+ "os dados para efetuar o pagamento são:\n\n"
				+ refData
				+ "\n\nApós efetuar o pagamento, o valor será transferido em aproximadamente 24h.\n"
				+ "Obrigado, Cantin@IPL";

		m.setTo(to);
		m.setFrom(from);
		m.setSubject(subject);
		m.setBody(emailBody);

		try {
			// m.addAttachment("/sdcard/filelocation");
			if (m.send()) {
				Toast.makeText(super.getContext(),
						"Email was sent successfully.", Toast.LENGTH_LONG)
						.show();
			} else {
				Toast.makeText(super.getContext(), "Email was not sent.",
						Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			Toast.makeText(super.getContext(),
					"There was a problem sending the email.", Toast.LENGTH_LONG)
					.show();
			Log.e("MailApp", "Could not send email", e);
		}
	}
}
