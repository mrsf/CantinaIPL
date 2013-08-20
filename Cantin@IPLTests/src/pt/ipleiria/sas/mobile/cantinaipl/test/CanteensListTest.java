package pt.ipleiria.sas.mobile.cantinaipl.test;

import com.jayway.android.robotium.solo.Solo;

import pt.ipleiria.sas.mobile.cantinaipl.CanteensActivity;
import android.test.ActivityInstrumentationTestCase2;

public class CanteensListTest extends ActivityInstrumentationTestCase2<CanteensActivity> {

	private Solo solo;
	
	public CanteensListTest() {
		super(CanteensActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		solo = new Solo(getInstrumentation(), getActivity());
	}
	
	public void testCanteenListLayout() {
		

	}

	protected void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}

}
