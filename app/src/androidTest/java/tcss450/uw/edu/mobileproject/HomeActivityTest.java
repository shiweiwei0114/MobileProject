/*
 * TCSS 450 - Mobile App Programming
 * @author Weiwei Shi, Kyle Doan
 * @version 1.1
 */

package tcss450.uw.edu.mobileproject;


import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

/**
 * The home page of activity of the app.
 *
 * @author Weiwei Shi
 * @version May 28, 2016
 */
public class HomeActivityTest extends ActivityInstrumentationTestCase2<HomeActivity> {

    private Solo solo;

    public HomeActivityTest() {
        super(HomeActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        //solo.finishOpenedActivities();

        try {
            solo.finalize();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        getActivity().finish();
        super.tearDown();
    }

    /**
     * Test to see if the QuestionListFragment shows up.
     * Also test the back button
     */
    public void testQuestionDetail() {
        solo.clickInRecyclerView(0);
        boolean foundQuestionDetail = solo.searchText("What is encapsulation");
        assertTrue("Question Detail fragment loaded", foundQuestionDetail);
        solo.goBack();
        boolean foundQuestionList = solo.searchText("Proterview");
        assertTrue("Back to list worked!", foundQuestionList);
    }

    /**
     * Test to see if add a question works
     */
    public void testQuestionAddButton() {
        //Go to addQuestion page by clicking on the fab button
        solo.clickOnView(getActivity().findViewById(R.id.add_question));

        //Enter question details
        solo.enterText(0, "What is heap sort?");
        solo.enterText(1, "Google");
        solo.enterText(2, "Data Structure");

        solo.clickOnButton("add question");
        boolean textFound = solo.searchText("What is heap sort?");
        assertTrue("Question added!", textFound);
    }
}
