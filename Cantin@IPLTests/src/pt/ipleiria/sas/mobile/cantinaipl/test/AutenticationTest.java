package pt.ipleiria.sas.mobile.cantinaipl.test;

import pt.ipleiria.sas.mobile.cantinaipl.AuthenticationActivity;
import pt.ipleiria.sas.mobile.cantinaipl.CanteensActivity;
import pt.ipleiria.sas.mobile.cantinaipl.R;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.jayway.android.robotium.solo.Solo;

public class AutenticationTest extends ActivityInstrumentationTestCase2<AuthenticationActivity> {
	
	private Solo solo;

	public AutenticationTest() {
		super(AuthenticationActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		solo = new Solo(getInstrumentation(), getActivity());
	}
	
	public void testAuthenticationLayout() {
		boolean actual;
		
		actual = solo.searchEditText("Login");
		assertTrue(actual);
		
		actual = solo.searchEditText("Password");
		assertTrue(actual);
		
		actual = solo.searchButton("Entrar");
		assertTrue(actual);
	}
	
	public void testEmptyUsername() {
		
		/*EditText userName = (EditText) solo.getView(R.id.et_login);
		solo.enterText(userName, "");*/
		solo.enterText(0, "");
		
		/*EditText userPassword = (EditText) solo.getView(R.id.et_password);
		solo.enterText(userPassword, "13225534");*/
		solo.enterText(1, "13225534");
		
		//solo.clickOnButton("Entrar");
		solo.clickOnButton(0);
		
		assertTrue(solo.waitForText("Preencher o Login."));
	}
	
	public void testEmptyPassword() {
		
		/*EditText userName = (EditText) solo.getView(R.id.et_login);
		solo.enterText(userName, "2091112");*/
		solo.enterText(0, "2091112");
		
		/*EditText userPassword = (EditText) solo.getView(R.id.et_password);
		solo.enterText(userPassword, "");*/
		solo.enterText(1, "");
		
		//solo.clickOnButton("Entrar");
		solo.clickOnButton(0);
		
		assertTrue(solo.waitForText("Preencher a Password."));
	}
	
	public void testInvalidUser() {
		
		/*EditText userName = (EditText) solo.getView(R.id.et_login);
		solo.enterText(userName, "2091112");*/
		solo.enterText(0, "2091112");
		
		/*EditText userPassword = (EditText) solo.getView(R.id.et_password);
		solo.enterText(userPassword, "2091112");*/
		solo.enterText(1, "2091112");
		
		//solo.clickOnButton("Entrar");
		solo.clickOnButton(0);
		
		assertTrue(solo.waitForText("Credenciais Inválidas."));
	}
	
	public void testValidUser() {
		
		/*EditText userName = (EditText) solo.getView(R.id.et_login);
		solo.enterText(userName, "2091112");*/
		solo.enterText(0, "2091112");
		
		/*EditText userPassword = (EditText) solo.getView(R.id.et_password);
		solo.enterText(userPassword, "13225534");*/
		solo.enterText(1, "13225534");
		
		//solo.clickOnButton("Entrar");
		solo.clickOnButton(0);
		
		assertTrue(solo.waitForActivity(CanteensActivity.class));
	}

	protected void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}

}
