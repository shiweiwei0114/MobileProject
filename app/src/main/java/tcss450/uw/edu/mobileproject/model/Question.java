package tcss450.uw.edu.mobileproject.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by KyleD on 4/29/16.
 */
public class Question implements Serializable {

    public static final String ID = "QuestID",
                    DETAIL = "QuestDetail",
                    EMAIL = "email",
                    COMPANY = "Company",
                    DATE = "DatePost";

    private String mQuestId;
    private String mQuestDetail;
    private String mUserEmail;
    private String mCompany;
    private List<String> mTags;
    private String mQuestDatePost;

    public Question(String id, String email, String now,
                    String questDetail, String company) {
        mQuestId = id;
        mUserEmail = email;
        mQuestDatePost = now;
        mQuestDetail = questDetail;
        mCompany = company;
        mTags = new LinkedList<>();
    }

    public String getId() {
        return mQuestId;
    }

    public String getQuestDetail() {
        return mQuestDetail;
    }

    public String getQuestDatePost() {
        return mQuestDatePost;
    }

    public String getUser() {
        return mUserEmail;
    }

    public List<String> getTags() {
        return new LinkedList<>(mTags);
    }

    public String getCompany() {
        return mCompany;
    }

    /**
     * Parses the json string, returns an error message if unsuccessful.
     * Returns question list if success.
     * @param questJSON
     * @param questsList
     * @return reason or null if successful.
     */
    public static String parseQuestionJSON(String questJSON, List<Question> questsList) {
        String reason = null;
        if (questJSON != null) {
            try {
                JSONArray arr = new JSONArray(questJSON);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    Question course = new Question(obj.getString(Question.ID),
                            obj.getString(Question.EMAIL),
                            obj.getString(Question.DATE),
                            obj.getString(Question.DETAIL),
                            obj.getString(Question.COMPANY));
                    questsList.add(course);
                }
            } catch (JSONException e) {
                reason = "Unable to parse data, Reason: " + e.getMessage();
            }
        }
        return reason;
    }
}
