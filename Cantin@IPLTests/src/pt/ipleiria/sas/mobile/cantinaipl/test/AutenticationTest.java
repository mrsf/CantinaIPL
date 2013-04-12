package pt.ipleiria.sas.mobile.cantinaipl.test;

import pt.ipleiria.sas.mobile.cantinaipl.CanteensActivity;
import pt.ipleiria.sas.mobile.cantinaipl.MainActivity;
import pt.ipleiria.sas.mobile.cantinaipl.R;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.jayway.android.robotium.solo.Solo;

public class AutenticationTest extends ActivityInstrumentationTestCase2<MainActivity> {
	
	private Solo solo;

	public AutenticationTest() {
		super(MainActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		solo = new Solo(getInstrumentation(), getActivity());
	}
	
	public void testAuthenticationLayout() {
		boolean actual;
		
		actual = solo.searchEditText("Username");
		assertTrue(actual);
		
		actual = solo.searchEditText("Password");
		assertTrue(actual);
		
		actual = solo.searchButton("Entrar");
		assertTrue(actual);
	}
	
	public void testEmptyUsername() {
		
		EditText userName = (EditText) solo.getView(R.id.loginEditText);
		solo.enterText(userName, "");
		
		EditText userPassword = (EditText) solo.getView(R.id.passwordEditText);
		solo.enterText(userPassword, "13225534");
		
		
		//solo.enterText(0, "");
		//solo.enterText(1, "13225534");
		
		solo.clickOnButton("Entrar");
		
		assertTrue(solo.waitForText("Preencher o Username."));
	}
	
	public void testEmptyPassword() {
		
		EditText userName = (EditText) solo.getView(R.id.loginEditText);
		solo.enterText(userName, "2091112");
		
		EditText userPassword = (EditText) solo.getView(R.id.passwordEditText);
		solo.enterText(userPassword, "");
		
		solo.clickOnButton("Entrar");
		
		assertTrue(solo.waitForText("Preencher a Password."));
	}
	
	public void testInvalidUser() {
		
		EditText userName = (EditText) solo.getView(R.id.loginEditText);
		solo.enterText(userName, "2091112");
		
		EditText userPassword = (EditText) solo.getView(R.id.passwordEditText);
		solo.enterText(userPassword, "2091112");
		
		solo.clickOnButton("Entrar");
		
		assertTrue(solo.waitForText("Utilizador/Password inválidos."));
	}
	
	public void testValidUser() {
		
		EditText userName = (EditText) solo.getView(R.id.loginEditText);
		solo.enterText(userName, "2091112");
		
		EditText userPassword = (EditText) solo.getView(R.id.passwordEditText);
		solo.enterText(userPassword, "13225534");
		
		solo.clickOnButton("Entrar");
		
		assertTrue(solo.waitForActivity(CanteensActivity.class));
	}

	protected void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}

}
