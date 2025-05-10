package com.example.assignment2;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiScrollable;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class AppUITest {

    private static final String PACKAGE_NAME = "com.example.assignment2";
    private static final int LAUNCH_TIMEOUT = 5000;
    private UiDevice device;

    @Before
    public void startMainActivityFromHomeScreen() {
        // Initialize UiDevice instance
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        // Start from the home screen
        device.pressHome();

        // Wait for launcher
        final String launcherPackage = getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        device.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);

        // Launch the app
        Context context = ApplicationProvider.getApplicationContext();
        final Intent intent = context.getPackageManager().getLaunchIntentForPackage(PACKAGE_NAME);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);    // Clear out any previous instances
        context.startActivity(intent);

        // Wait for the app to appear
        device.wait(Until.hasObject(By.pkg(PACKAGE_NAME).depth(0)), LAUNCH_TIMEOUT);
    }

    @Test
    public void checkSecondActivityChallenge() throws UiObjectNotFoundException {
        // Find and click the "Start Activity Explicitly" button
        UiObject startButton = device.findObject(new UiSelector()
                .text("Start Activity Explicitly")
                .className("android.widget.Button"));

        // Verify button exists and click it
        assertTrue("'Start Activity Explicitly' button not found", startButton.exists());
        startButton.click();

        // Wait for the second activity to appear with a longer timeout
        device.wait(Until.hasObject(By.pkg(PACKAGE_NAME)), LAUNCH_TIMEOUT * 2);

        // Create a scrollable object to search through the screen
        UiScrollable scrollable = new UiScrollable(new UiSelector().scrollable(true));
        scrollable.setAsVerticalList();

        // Check for the title first to confirm we're in the right activity
        UiObject titleText = device.findObject(new UiSelector()
                .text("Mobile Software Engineering Challenges"));

        // If title not found, try scrolling to find it
        if (!titleText.exists() && scrollable.exists()) {
            scrollable.scrollIntoView(titleText);
        }

        // Create a list of exact challenges to look for (from the layout file)
        String[] challenges = {
                "1. Rapid changes",
                "2. Low tolerance",
                "3. Unstable and Dynamic Environment",
                "4. Tool Support",
                "5. Low Security and Privacy Awareness"
        };

        // Check if at least one challenge exists in the second activity
        boolean foundChallenge = false;

        // Try to find each challenge with exact text matching
        for (String challenge : challenges) {
            UiObject challengeText = device.findObject(new UiSelector().text(challenge));

            // If not immediately visible, try scrolling to it
            if (!challengeText.exists() && scrollable.exists()) {
                try {
                    scrollable.scrollIntoView(new UiSelector().text(challenge));
                    if (challengeText.exists()) {
                        foundChallenge = true;
                        break;
                    }
                } catch (UiObjectNotFoundException e) {
                    // Continue with next challenge if scrolling fails
                    continue;
                }
            } else if (challengeText.exists()) {
                foundChallenge = true;
                break;
            }
        }

        // If exact matching fails, try with partial matching as a fallback
        if (!foundChallenge) {
            // Try partial matching on key phrases
            String[] keyPhrases = {"Rapid", "tolerance", "Unstable", "Tool Support", "Security"};
            for (String phrase : keyPhrases) {
                UiObject challengeText = device.findObject(new UiSelector().textContains(phrase));

                // If not immediately visible, try scrolling to it
                if (!challengeText.exists() && scrollable.exists()) {
                    try {
                        scrollable.scrollIntoView(new UiSelector().textContains(phrase));
                        if (challengeText.exists()) {
                            foundChallenge = true;
                            break;
                        }
                    } catch (UiObjectNotFoundException e) {
                        // Continue with next phrase if scrolling fails
                        continue;
                    }
                } else if (challengeText.exists()) {
                    foundChallenge = true;
                    break;
                }
            }
        }

        // Assert that at least one challenge was found
        assertTrue("No mobile software engineering challenge found in the second activity", foundChallenge);
    }

    /**
     * Helper method to get the launcher package name
     */
    private String getLauncherPackageName() {
        // Create launcher Intent
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);

        // Use PackageManager to get the launcher package name
        PackageManager pm = ApplicationProvider.getApplicationContext().getPackageManager();
        ResolveInfo resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo.activityInfo.packageName;
    }
}