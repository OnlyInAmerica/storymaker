
package info.guardianproject.mrapp.test;

import android.test.ActivityInstrumentationTestCase2;

import com.slidingmenu.lib.SlidingMenu;

import info.guardianproject.mrapp.HomeActivity;
import info.guardianproject.mrapp.R;
import info.guardianproject.mrapp.test.actions.OpenSlidingMenuAction;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isAssignableFrom;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withClassName;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static info.guardianproject.mrapp.test.util.Util.handleStoryMakerStartupDialogs;
import static org.hamcrest.Matchers.containsString;

/**
 * Create a quick video recording via the SlidingMenu's
 * btnDrawerQuickCaptureVideo Button
 *
 * Note: This test ends outside the storymaker app
 * when the CAPTURE_VIDEO intent resolves to the
 * system's corresponding Activity. As such it is not
 * currently very useful.
 * 
 * @author davidbrodsky
 */
public class TestCreateVideo extends ActivityInstrumentationTestCase2<HomeActivity> {

    public TestCreateVideo() {
        super(HomeActivity.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        getActivity();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void test() {

        handleStoryMakerStartupDialogs();

        // Reveal SlidingMenu and click the video capture button
        onView(isAssignableFrom(SlidingMenu.class)).perform(new OpenSlidingMenuAction());
        onView(withId(R.id.btnDrawerQuickCaptureVideo)).perform(click());

        // The OverlayCameraActivity appears. Click the overlay ImageView
        onView(withClassName(containsString("ImageView"))).perform(click());

        // The CAPTURE_VIDEO intent resolves. Unfortunately,
        // this ends the Espresso test and we can no longer test.

    }

}
