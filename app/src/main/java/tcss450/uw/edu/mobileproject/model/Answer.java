/*
 * TCSS 450 - Mobile App Programming
 * @author Weiwei Shi, Kyle Doan
 * @version 1.1
 */

package tcss450.uw.edu.mobileproject.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Answer Details.
 *
 * @author Kyle Doan
 * @version May 5, 2016
 */
public class Answer {
    private static final String LOG = "Answer";
    public static final String ANS_ID = "AnsID",
            QUEST_ID = "QuestID",
            EMAIL = "email",
            ANS_DETAIL = "AnsDetail",
            DATE = "DatePost";

    private String mAnsID, mQuestID, mUser, mDatePost, mAnsDetail;

    /**
     * Answer Constructor.
     *
     * @param ansId     the Answer id
     * @param questId   the question id
     * @param user      the user who answer the question
     * @param date      the date the user answer the question
     * @param ansDetail the answer
     */
    public Answer(String ansId, String questId, String user,
                  String date, String ansDetail) {
        mAnsID = ansId;
        mQuestID = questId;
        mUser = user;
        mDatePost = date;
        mAnsDetail = ansDetail;
    }

    /**
     * Get Answer id.
     *
     * @return Answer id.
     */
    public String getAnsId() {
        return mAnsID;
    }

    /**
     * Get Question id.
     *
     * @return Question id.
     */
    public String getQuestId() {
        return mQuestID;
    }

    /**
     * Get the user.
     *
     * @return the user.
     */
    public String getUser() {
        return mUser;
    }

    /**
     * Get answer posted date.
     *
     * @return answer posted date.
     */
    public String getAnsDatePost() {
        return mDatePost;
    }

    /**
     * Ger answer details
     *
     * @return answer details
     */
    public String getAnsDetail() {
        return mAnsDetail;
    }

    /**
     * Parses the json string, returns an error message if unsuccessful.
     * Returns question list if success.
     *
     * @param ansJSON JSAON file
     * @param ansList a list
     * @return reason or null if successful.
     */
    public static String parseAnswerJSON(String ansJSON, List<Answer> ansList) {
        String reason = null;
//        Log.i(LOG, "ansJSON is " + ansJSON.length());
        if (ansJSON.length() > 0) {
            try {
                JSONArray arr = new JSONArray(ansJSON);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    Answer ans = new Answer(obj.getString(Answer.ANS_ID),
                            obj.getString(Answer.QUEST_ID),
                            obj.getString(Answer.EMAIL),
                            obj.getString(Answer.DATE),
                            obj.getString(Answer.ANS_DETAIL));
                    ansList.add(ans);
                }
            } catch (JSONException e) {
                Log.i(LOG, "reason is " + e.getMessage());
                reason = "Unable to parse answer data, Reason: " + e.getMessage();
            }
        }
        return reason;
    }
}
