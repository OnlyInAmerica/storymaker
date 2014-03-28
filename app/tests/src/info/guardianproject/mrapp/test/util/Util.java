package info.guardianproject.mrapp.test.util;

import com.google.android.apps.common.testing.ui.espresso.NoMatchingViewException;

import net.hockeyapp.android.Strings;

import info.guardianproject.mrapp.R;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;

public class Util {

    /**
     * Dismiss StoryMaker startup dialogs so tests may proceed.
     *
     * Checks for the HockeyAppSendCrashDialog twice in case
     * it is present behind the EULA.
     *
     * Note: The revised StoryMaker flow will remove the EULA
     * showing without user interaction. At that point the handleEula()
     * call may be removed from this method.
     */
    public static void handleStoryMakerStartupDialogs() {
        handleHockeyAppSendCrashDialog();
        handleEula();
        handleHockeyAppSendCrashDialog();
    }

    /**
     * Agree to the EULA dialog if present.
     * Catches and ignores a
     * {@link com.google.android.apps.common.testing.ui.espresso.NoMatchingViewException }
     * that may result from the Eula not being present
     */
    private static void handleEula() {
        try {
            if (onView(withText(R.string.eula_title)) != null) {
                onView(withText(R.string.eula_accept)).perform(click());
            }
        } catch (NoMatchingViewException e) {
            // Eula not present
        }
    }

    /**
     * Dismiss the Hockey App's Send / Dismiss crash
     * data Dialog if present.
     * Catches and ignores a
     * {@link com.google.android.apps.common.testing.ui.espresso.NoMatchingViewException }
     * that may result from the Dialog not being present
     */
    private static void handleHockeyAppSendCrashDialog() {
        try {
            if (onView(withText(Strings.get(Strings.CRASH_DIALOG_TITLE_ID))) != null) {
                onView(withText(Strings.get(Strings.CRASH_DIALOG_NEGATIVE_BUTTON_ID))).perform(click());
            }
        } catch (NoMatchingViewException e) {
            // Dialog not present
        }
    }


}