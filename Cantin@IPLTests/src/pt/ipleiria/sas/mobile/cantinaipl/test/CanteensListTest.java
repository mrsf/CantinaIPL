package pt.ipleiria.sas.mobile.cantinaipl.test;

import com.jayway.android.robotium.solo.Solo;

import pt.ipleiria.sas.mobile.cantinaipl.AccountActivity;
import pt.ipleiria.sas.mobile.cantinaipl.AuthenticationActivity;
import pt.ipleiria.sas.mobile.cantinaipl.CanteensActivity;
import pt.ipleiria.sas.mobile.cantinaipl.MealsActivity;
import pt.ipleiria.sas.mobile.cantinaipl.ReservesActivity;
import pt.ipleiria.sas.mobile.cantinaipl.SettingsActivity;
import android.test.ActivityInstrumentationTestCase2;

public class CanteensListTest extends
		ActivityInstrumentationTestCase2<CanteensActivity> {

	private Solo solo;

	public CanteensListTest() {
		super(CanteensActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		solo = new Solo(getInstrumentation(), getActivity());
	}

	public void testActionBarAccountIcon() {

		assertTrue(solo.waitForActivity(AuthenticationActivity.class));

		solo.clearEditText(0);
		solo.enterText(0, "2091112");
		solo.clearEditText(1);
		solo.enterText(1, "13225534");
		solo.clickOnCheckBox(0);
		solo.clickOnButton(0);
		assertTrue(solo.waitForActivity(CanteensActivity.class));

		solo.clickOnActionBarItem(pt.ipleiria.sas.mobile.cantinaipl.R.id.action_account);
		assertTrue(solo.waitForActivity(AccountActivity.class));

	}

	public void testActionBarReservesIcon() {

		assertTrue(solo.waitForActivity(AuthenticationActivity.class));

		solo.clearEditText(0);
		solo.enterText(0, "2091112");
		solo.clearEditText(1);
		solo.enterText(1, "13225534");
		solo.clickOnCheckBox(0);
		solo.clickOnButton(0);
		assertTrue(solo.waitForActivity(CanteensActivity.class));

		solo.clickOnActionBarItem(pt.ipleiria.sas.mobile.cantinaipl.R.id.action_reserves);
		assertTrue(solo.waitForActivity(ReservesActivity.class));

	}

	public void testActionBarSettingsIcon() {

		assertTrue(solo.waitForActivity(AuthenticationActivity.class));

		solo.clearEditText(0);
		solo.enterText(0, "2091112");
		solo.clearEditText(1);
		solo.enterText(1, "13225534");
		solo.clickOnCheckBox(0);
		solo.clickOnButton(0);
		assertTrue(solo.waitForActivity(CanteensActivity.class));

		solo.clickOnActionBarItem(pt.ipleiria.sas.mobile.cantinaipl.R.id.action_settings);
		assertTrue(solo.waitForActivity(SettingsActivity.class));

	}

	public void testBackButton() {

		assertTrue(solo.waitForActivity(AuthenticationActivity.class));

		solo.clearEditText(0);
		solo.enterText(0, "2091112");
		solo.clearEditText(1);
		solo.enterText(1, "13225534");
		solo.clickOnCheckBox(0);
		solo.clickOnButton(0);
		assertTrue(solo.waitForActivity(CanteensActivity.class));

		solo.goBack();
		assertTrue(solo.waitForDialogToOpen(5));
		
		assertTrue(solo.waitForText("Sair da aplicação"));
		assertTrue(solo.waitForText("Sim"));
		solo.clickOnText("Sim");

	}
	
	public void testClickOnValidListItem() {

		assertTrue(solo.waitForActivity(AuthenticationActivity.class));

		solo.clearEditText(0);
		solo.enterText(0, "2091112");
		solo.clearEditText(1);
		solo.enterText(1, "13225534");
		solo.clickOnCheckBox(0);
		solo.clickOnButton(0);
		assertTrue(solo.waitForActivity(CanteensActivity.class));

		solo.clickInList(1);
		assertTrue(solo.waitForActivity(MealsActivity.class));

	}
	
	public void testClickOnInvalidListItem() {

		assertTrue(solo.waitForActivity(AuthenticationActivity.class));

		solo.clearEditText(0);
		solo.enterText(0, "2091112");
		solo.clearEditText(1);
		solo.enterText(1, "13225534");
		solo.clickOnCheckBox(0);
		solo.clickOnButton(0);
		assertTrue(solo.waitForActivity(CanteensActivity.class));

		solo.clickInList(6);
		assertFalse(solo.waitForActivity(MealsActivity.class));

	}

	protected void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}

}
