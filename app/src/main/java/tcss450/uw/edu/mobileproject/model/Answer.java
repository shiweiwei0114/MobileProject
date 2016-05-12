package tcss450.uw.edu.mobileproject.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by KyleD on 5/11/16.
 *
 */
public class Answer {
    private static final String LOG = "Answer";
    public static final String ANS_ID = "AnsID",
            QUEST_ID = "QuestID",
            EMAIL = "email",
            ANS_DETAIL = "AnsDetail",
            DATE = "DatePost";

    private String mAnsID, mQuestID, mUser, mDatePost, mAnsDetail;

    public Answer(String ansId, String questId, String user,
                  String date, String ansDetail) {
        mAnsID = ansId;
        mQuestID = questId;
        mUser = user;
        mDatePost = date;
        mAnsDetail = ansDetail;
    }

    public String getAnsId() {return mAnsID;}
    public String getQuestId() {return mQuestID;}
    public String getUser() {return mUser;}
    public String getAnsDatePost() {return mDatePost;}
    public String getAnsDetail() {return mAnsDetail;}

    /**
     * Parses the json string, returns an error message if unsuccessful.
     * Returns question list if success.
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
