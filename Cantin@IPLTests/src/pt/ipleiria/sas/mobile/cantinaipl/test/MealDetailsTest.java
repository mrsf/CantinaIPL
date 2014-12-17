package pt.ipleiria.sas.mobile.cantinaipl.test;

import com.jayway.android.robotium.solo.Solo;

import pt.ipleiria.sas.mobile.cantinaipl.AuthenticationActivity;
import pt.ipleiria.sas.mobile.cantinaipl.CanteensActivity;
import pt.ipleiria.sas.mobile.cantinaipl.MealDetailsActivity;
import pt.ipleiria.sas.mobile.cantinaipl.MealsActivity;
import android.test.ActivityInstrumentationTestCase2;

public class MealDetailsTest extends ActivityInstrumentationTestCase2<MealDetailsActivity> {

	private Solo solo;

	public MealDetailsTest() {
		super(MealDetailsActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		solo = new Solo(getInstrumentation(), getActivity());
	}
	
	public void testeMealValidReserve() {
		
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
		
		solo.clickInList(1, 0);
		
		assertTrue(solo.waitForActivity(MealDetailsActivity.class));
		
		solo.clickOnButton(1);
		
		assertTrue(solo.waitForText("A efectuar reserva"));
		
		assertTrue(solo.waitForText("Reserva efectuada com sucesso"));
		
		
	}
	
	public void testeMealInvalidReserve() {
		
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
		
		solo.clickInList(2, 0);
		
		assertTrue(solo.waitForActivity(MealDetailsActivity.class));
		
		solo.clickOnButton(1);
		
		assertTrue(solo.waitForText("A efectuar reserva"));
		
		assertTrue(solo.waitForText("Não é possivel efectuar a reserva"));
		
		
	}
	
	protected void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}
	
}
