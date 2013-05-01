package pt.ipleiria.sas.mobile.cantinaipl;

import pt.ipleiria.sas.mobile.cantinaipl.model.User;
import pt.ipleiria.sas.mobile.cantinaipl.service.AuthenticationService;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private AuthenticationService authenticationService;
	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.authentication);
		
		EditText userEditText = (EditText) findViewById(R.id.loginEditText);
		EditText passEditText = (EditText) findViewById(R.id.passwordEditText);
		userEditText.setText("2091112");
		passEditText.setText("13225534");

		Button bt = (Button) findViewById(R.id.startButton);
		bt.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		EditText userEditText = (EditText) findViewById(R.id.loginEditText);
		EditText passEditText = (EditText) findViewById(R.id.passwordEditText);

		user = new User(userEditText.getText().toString(), passEditText
				.getText().toString());
		authenticationService = new AuthenticationService(user, this);
	}

	public void testAuthentication(boolean result, String messenge) {
		if (result == true) {
			Toast.makeText(this, messenge, Toast.LENGTH_LONG);
			Intent it = new Intent(this, MealsActivity.class);
			this.startActivity(it);
		} else if (result == false) {
			Toast.makeText(this, "Utilizador/Password inválidos.", Toast.LENGTH_LONG).show();
		}
		authenticationService = null;
	}

	public AuthenticationService getAuthenticationService() {
		return authenticationService;
	}

	public void setAuthenticationService(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

}
