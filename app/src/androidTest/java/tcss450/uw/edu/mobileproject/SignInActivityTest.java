/*
 * TCSS 450 - Mobile App Programming
 * @author Weiwei Shi, Kyle Doan
 * @version 1.1
 */
package tcss450.uw.edu.mobileproject;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import tcss450.uw.edu.mobileproject.authenticate.SignInActivity;

/**
 * Created by Weiwei Shi on 5/28/2016.
 * Note: If you would like to test this class, please make sure you log out first.
 * Because we used SharedPreference to store sign-in information.
 */
public class SignInActivityTest extends ActivityInstrumentationTestCase2<SignInActivity> {

    private Solo solo;

    public SignInActivityTest() {
        super(SignInActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }


    /**
     * Test if the Login loads
     */
    public void testLogInLoads() {
        boolean emailTextViewLoaded = solo.searchText("Enter Email");
        boolean logInButtonLoaded = solo.searchButton("Log In");
        assertTrue("Enter Email TextView Loaded", emailTextViewLoaded);
        assertTrue("Log In loaded", logInButtonLoaded);
    }

    /**
     * Test if login works.
     */
    public void testSignInWorks() {
        solo.enterText(0, "wshi@uw.edu");
        solo.enterText(1, "password");
        solo.clickOnButton("Log In");
        boolean worked = solo.searchText("Proterview");
        assertTrue("Sign in worked!", worked);
    }

}
