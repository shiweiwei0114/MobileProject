package tcss450.uw.edu.mobileproject;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import tcss450.uw.edu.mobileproject.model.Question;

/**
 * Created by KyleD on 5/31/16.
 */
public class QuestionTest extends TestCase{

    private Question mQuest;

    @Before
    public void setUp() {
        mQuest = new Question("10", "k@uw.edu", "2016-05-04 14:18:05",
                "What is encapsulation?", "UWT");
    }

    @Test
    public void testSetNullIdQuestion() {
        try {
            mQuest.setId(null);
            fail("Question Id can be set to null");
        } catch (IllegalArgumentException e) {

        }
    }

    @Test
    public void testSetEmptyIdQuestion() {
        try {
            mQuest.setId("");
            fail("Question Id can be set to empty string");
        } catch (IllegalArgumentException e) {

        }
    }

    @Test
    public void testGetId() {
        assertEquals(mQuest.getId(), "10");
    }

    @Test
    public void testSetId() {
        mQuest.setId("15");
        assertEquals(mQuest.getId(), "15");
    }

    @Test
    public void testSetNullEmail() {
        try {
            mQuest.setEmail(null);
            fail("Question email can be set to null");
        } catch (IllegalArgumentException e) {

        }
    }

    @Test
    public void testSetEmptyEmail() {
        try {
            mQuest.setEmail("");
            fail("Question email can be set to empty string");
        } catch (IllegalArgumentException e) {

        }
    }

    @Test
    public void testSetEmailWithoutEmailSymbol() {
        try {
            mQuest.setEmail("kuw.edu");
            fail("Question email can be set not to have email symbol - @");
        } catch (IllegalArgumentException e) {

        }
    }

    @Test
    public void testGetEmail() {
        assertEquals(mQuest.getUser(), "k@uw.edu");
    }

    @Test
    public void testSetEmail() {
        String newEmail = "kdoan91@uw.edu";
        mQuest.setEmail(newEmail);
        assertEquals(mQuest.getUser(), newEmail);
    }

    @Test
    public void testSetNullDate() {
        try {
            mQuest.setDate(null);
            fail("Question date can be set to null");
        } catch (IllegalArgumentException e) {

        }
    }

    @Test
    public void testGetDate() {
        assertEquals(mQuest.getQuestDatePost(), "2016-05-04 14:18:05");
    }

    @Test
    public void testSetDate() {
        String newTime = "2016-06-01 8:41:05";
        mQuest.setDate(newTime);
        assertEquals(mQuest.getQuestDatePost(), newTime);
    }

    @Test
    public void testSetNullQuestDetail() {
        try {
            mQuest.setQuestDetail(null);
            fail("Question detail can be set to null");
        } catch (IllegalArgumentException e) {

        }
    }

    @Test
    public void testSetEmptyQuestDetail() {
        try {
            mQuest.setQuestDetail("");
            fail("Question detail can be set to empty");
        } catch (IllegalArgumentException e) {

        }
    }

    @Test
    public void testGetQuestDetail() {
        assertEquals(mQuest.getQuestDetail(), "What is encapsulation?");
    }

    @Test
    public void testSetQuestDetail() {
        String newDetail = "What is dynamic programming?";
        mQuest.setQuestDetail(newDetail);
        assertEquals(mQuest.getQuestDetail(), newDetail);
    }

    @Test
    public void testSetNullCompany() {
        try {
            mQuest.setCompany(null);
            fail("Question company can be set to null");
        } catch (IllegalArgumentException e) {

        }
    }

    @Test
    public void testSetEmptyCompany() {
        try {
            mQuest.setCompany("");
            fail("Question company can be set to empty");
        } catch (IllegalArgumentException e) {

        }
    }

    @Test
    public void testGetCompany() {
        assertEquals(mQuest.getCompany(), "UWT");
    }

    @Test
    public void testSetCompany() {
        String newCompany = "Seattle";
        mQuest.setCompany(newCompany);
        assertEquals(mQuest.getCompany(), newCompany);
    }

    @Test
    public void testParseQuestionJSON() {
        String questionJSON = "[{\"QuestID\":\"100\",\"email\":\"k@uw.edu\"," +
                "\"QuestDatePost\":\"2016-05-04 14:18:05\",\"QuestDetail\":\"How google search works?\"," +
                "\"Company\":\"Google\"}," +
                "{\"QuestID\":\"101\",\"email\":\"k1@uw.edu\"," +
                "\"QuestDatePost\":\"2016-05-04 14:21:05\",\"QuestDetail\":\"How facebook search works?\"," +
                "\"Company\":\"facebook\"}]";
        String message = Question.parseQuestionJSON(questionJSON, new ArrayList<Question>());
        assertTrue("JSON With Valid String", message == null);
    }

    @Test
    public void testParseTagJSON() {
        String tagJSON = "[{\"TagName\":\"Algorithm\",\"QuestID\":\"22\"}," +
                "{\"TagName\":\"Algorithm\",\"QuestID\":\"29\"}]";
        String message = Question.parseTagsQuestionJSON(tagJSON, new ArrayList<String>());
        assertTrue("JSON With Valid String", message == null);
    }
}
