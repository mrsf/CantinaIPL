package pt.ipleiria.sas.mobile.cantinaipl.test;

import com.jayway.android.robotium.solo.Solo;

import pt.ipleiria.sas.mobile.cantinaipl.MealsActivity;
import android.test.ActivityInstrumentationTestCase2;

public class MealsListsTest extends ActivityInstrumentationTestCase2<MealsActivity> {

	private Solo solo;

	public MealsListsTest() {
		super(MealsActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		solo = new Solo(getInstrumentation(), getActivity());
	}
	
	public void testeMeals() {

		
	}
	
	protected void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}
	
}
