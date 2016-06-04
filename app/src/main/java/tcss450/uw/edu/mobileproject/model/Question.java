/*
 * TCSS 450 - Mobile App Programming
 * @author Weiwei Shi, Kyle Doan
 * @version 1.0
 */

package tcss450.uw.edu.mobileproject.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * The content of the question.
 *
 * @author KyleD, Weiwei
 * @version May 5, 2016
 */
public class Question implements Serializable {

    public static final String ID = "QuestID",
            DETAIL = "QuestDetail",
            EMAIL = "email",
            COMPANY = "Company",
            DATE = "DatePost";

    public static final String TAG_NAME = "TagName";

    private String mQuestId;
    private String mQuestDetail;
    private String mUserEmail;
    private String mCompany;
    private String mQuestDatePost;

    /**
     * Constructor
     *
     * @param theId          the id
     * @param theEmail       the email
     * @param theNow         the date and time
     * @param theQuestDetail the question details
     * @param theCompany     the company names
     */
    public Question(String theId, String theEmail, String theNow,
                    String theQuestDetail, String theCompany) {
        mQuestId = theId;
        mUserEmail = theEmail;
        mQuestDatePost = theNow;
        mQuestDetail = theQuestDetail;
        mCompany = theCompany;
    }

    /**
     * Set Question id.
     *
     * @param id Question id.
     */
    public void setId(String id) {
        if (id == null || id.length() == 0) {
            throw new IllegalArgumentException();
        }
        mQuestId = id;
    }

    /**
     * Set Email.
     *
     * @param email user email
     */
    public void setEmail(String email) {
        if (email == null || email.length() < 5 || !email.contains("@")) {
            throw new IllegalArgumentException();
        }
        mUserEmail = email;
    }

    /**
     * Set the question posted date and time.
     *
     * @param time the question posted date and time.
     */
    public void setDate(String time) {
        if (time == null) {
            throw new IllegalArgumentException();
        }
        mQuestDatePost = time;
    }

    /**
     * Set question details
     *
     * @param questDetail the question details
     */
    public void setQuestDetail(String questDetail) {
        if (questDetail == null || questDetail.length() < 5) {
            throw new IllegalArgumentException();
        }
        mQuestDetail = questDetail;
    }

    /**
     * Set company
     *
     * @param company the company
     */
    public void setCompany(String company) {
        if (company == null || company.length() == 0) {
            throw new IllegalArgumentException();
        }
        mCompany = company;
    }

    /**
     * Get Id.
     *
     * @return the Id.
     */
    public String getId() {
        return mQuestId;
    }

    /**
     * Get Question Detail.
     *
     * @return question detail.
     */
    public String getQuestDetail() {
        return mQuestDetail;
    }

    /**
     * Get Question post date.
     *
     * @return question post date.
     */
    public String getQuestDatePost() {
        return mQuestDatePost;
    }

    /**
     * Get the usr Email.
     *
     * @return the user email.
     */
    public String getUser() {
        return mUserEmail;
    }


    /**
     * Get the company name.
     *
     * @return the company name.
     */
    public String getCompany() {
        return mCompany;
    }

    /**
     * Parses the json string, returns an error message if unsuccessful.
     * Returns question list if success.
     *
     * @param questJSON  JSAON file
     * @param questsList a list
     * @return reason or null if successful.
     */
    public static String parseQuestionJSON(String questJSON, List<Question> questsList) {
        String reason = null;
        if (questJSON != null) {
            try {
                JSONArray arr = new JSONArray(questJSON);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    Question quest = new Question(obj.getString(Question.ID),
                            obj.getString(Question.EMAIL),
                            obj.getString(Question.DATE),
                            obj.getString(Question.DETAIL),
                            obj.getString(Question.COMPANY));
                    questsList.add(quest);
                }
            } catch (JSONException e) {
                reason = "Unable to parse question data, Reason: " + e.getMessage();
            }
        }
        return reason;
    }

    /**
     * Parses the json string, returns an error message if unsuccessful.
     * Returns tag list if success.
     *
     * @param tagJSON JSAON file
     * @param tags    a list
     * @return reason or null if successful.
     */
    public static String parseTagsQuestionJSON(String tagJSON, List<String> tags) {
        String reason = null;
        if (tagJSON != null) {
            try {
                JSONArray arr = new JSONArray(tagJSON);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    String tag = obj.getString(Question.TAG_NAME);
                    tags.add(tag);
                }
            } catch (JSONException e) {
                reason = "Unable to parse tags data, Reason: " + e.getMessage();
            }
        }
        return reason;
    }
}
