/*
 * TCSS 450 - Mobile App Programming
 * @author Weiwei Shi, Kyle Doan
 * @version 1.0
 */
package tcss450.uw.edu.mobileproject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import tcss450.uw.edu.mobileproject.model.Answer;
import tcss450.uw.edu.mobileproject.model.Question;


/**
 * A simple {@link Fragment} subclass show the post of questions.
 * Use the {@link QuestionPostFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * @author KyleD
 * @version 1.0
 */
public class QuestionPostFragment extends Fragment {

    public static String QUEST_SELECTED = "quest", USER = "user";
    public static final String TAG_URL =
            "http://cssgate.insttech.washington.edu/~_450btm7/test.php?cmd=question_tags";
    public static final String ANS_URL =
            "http://cssgate.insttech.washington.edu/~_450btm7/test.php?cmd=answers";
    public static final String ADD_ANS_URL =
            "http://cssgate.insttech.washington.edu/~_450btm7/addAnswer.php?";
    private final String LOG = "QuestionPostFragment";

    private TextView mQuestDetailTextView;
    private TextView mQuestUserPostTextView;
    private TextView mQuestDatePostTextView;
    private TextView mQuestCompanyTextView;
    private LinearLayout mTagsContainer;
    private LinearLayout mAnsContainer;
    private EditText mAnsEditText;

    private List<String> mTags;
    private List<Answer> mAnswersList;

    private String mSavedAns;
    private String mUserEmail;
    private String mQuestID;

    /**
     * Required empty public constructor
     */
    public QuestionPostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QuestionPostFragment.
     */
    public static QuestionPostFragment newInstance(String param1, String param2) {
        QuestionPostFragment fragment = new QuestionPostFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Called when the activity is starting.
     *
     * @param savedInstanceState contains the data it most recently supplied.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Called when the Fragment is visible to the user.
     */
    @Override
    public void onStart() {
        super.onStart();

        // During startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.
        Bundle args = getArguments();
        if (args != null) {
            // Set article based on argument passed in
            mUserEmail = args.getString(USER);
            updateView((Question) args.getSerializable(QUEST_SELECTED));
        }
    }

    /**
     * Update the question.
     * @param quest the question be sent.
     */
    public void updateView(Question quest) {
        if (quest != null) {
            mQuestID = quest.getId();
            mQuestDetailTextView.setText(quest.getQuestDetail());
            mQuestDetailTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
                    getResources().getDimensionPixelSize(R.dimen.text_size));
            mQuestCompanyTextView.setText(quest.getCompany());
            setDatePostTextView(quest.getQuestDatePost(), mQuestDatePostTextView);
            setUserTextView(quest.getUser(), mQuestUserPostTextView);
            downloadTagsAndAns(quest);
        }
    }

    private void downloadTagsAndAns(Question quest) {
        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            DownloadTagsTask task = new DownloadTagsTask();
            DownloadAnswersTask ansTask = new DownloadAnswersTask();
            String questTag = TAG_URL + "&questID=" + quest.getId();
            String ansUrl = ANS_URL + "&questID=" + quest.getId();
            task.execute(questTag);
            ansTask.execute(ansUrl);
        } else {
            Log.i(LOG, "No network available. Cannot display courses");
        }
    }

    private void setTagsView() {
        if (mTagsContainer.getChildCount() == 0) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 15, 0);  // left, top, right, bottom
            for (int i = 0; i < mTags.size(); i++) {
                TextView tagView = new TextView(this.getActivity());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tagView.setBackground(getResources().getDrawable(R.drawable.round_rect, null));
                }
                tagView.setLayoutParams(params);
                tagView.setText(mTags.get(i));
                mTagsContainer.addView(tagView);
            }
        }
    }

    private void setAnsList() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        for (int i = 0; i < mAnswersList.size(); i++) {
            // Display User TextView
            TextView userTextView = new TextView(this.getActivity());
            params.setMargins(10, 0, 0, 5);
            userTextView.setLayoutParams(params);
            setUserTextView(mAnswersList.get(i).getUser(), userTextView);
            mAnsContainer.addView(userTextView);

            // Display Answer TextView
            TextView answerTextView = new TextView(this.getActivity());
            params.setMargins(10, 0, 0, 0);
            answerTextView.setLayoutParams(params);
            answerTextView.setText(mAnswersList.get(i).getAnsDetail());
            mAnsContainer.addView(answerTextView);

            // Display DiffTime TextView
            TextView timeTextView = new TextView(this.getActivity());
            params.setMargins(10, 0, 0, 5);
            timeTextView.setLayoutParams(params);
            setDatePostTextView(mAnswersList.get(i).getAnsDatePost(), timeTextView);
            mAnsContainer.addView(timeTextView);
        }
    }

    private void setUserTextView(String user, TextView textView) {
        String[] userDisplay = user.split("@");
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setText(userDisplay[0]);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
                getResources().getDimensionPixelSize(R.dimen.text_size));
    }

    private void setDatePostTextView(String time, TextView textView) {
        Calendar now = Calendar.getInstance();
        Timestamp timePost = Timestamp.valueOf(time);
        long timeDif = now.getTimeInMillis() - timePost.getTime();  // millisecond
        timeDif /= 1000;   // second
        String timeDisplay = "";
//        Log.i(LOG, "Time Different: " + String.valueOf(timeDif));
        if (timeDif < 60) {
            timeDisplay = "Just now";
        } else if (timeDif < 3600) {    // less than 1 hour, using minute
            timeDif /= 60;  // minute
            timeDisplay = String.valueOf(timeDif) + " minutes ago";
        } else if (timeDif < 86400) { // less than 1 day, using hour
            timeDif /= 3600;    // hour
            timeDisplay = String.valueOf(timeDif) + " hours ago";
        } else if (timeDif < 2592000) {
            timeDif /= 86400;
            timeDisplay = String.valueOf(timeDif) + " days ago";
        }
        textView.setText(timeDisplay);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, // DIP : Device Indepentdent pixel
                getResources().getDimensionPixelSize(R.dimen.text_time_size));
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_question_post, container, false);
        // initialize all the id
        mQuestDetailTextView = (TextView) view.findViewById(R.id.question_detail);
        mQuestUserPostTextView = (TextView) view.findViewById(R.id.question_user_post);
        mQuestDatePostTextView = (TextView) view.findViewById(R.id.question_date_post);
        mQuestCompanyTextView = (TextView) view.findViewById(R.id.question_company);
        mTagsContainer = (LinearLayout) view.findViewById(R.id.tags_containter);
        mAnsContainer = (LinearLayout) view.findViewById(R.id.ans_container);
        mAnsEditText = (EditText) view.findViewById(R.id.ans_text);

        Button ans_btn = (Button) view.findViewById(R.id.ans_btn);
        ans_btn.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                Activity act = getActivity();
                InputMethodManager inputManager =
                        (InputMethodManager) act.getSystemService(act.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(act.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                String url = buildAnswerURL();
                AddAnswerTask task = new AddAnswerTask();
                task.execute(url);
            }
        });
        FloatingActionButton floatingActionButton = (FloatingActionButton)
                getActivity().findViewById(R.id.add_question);
        floatingActionButton.hide();
        return view;
    }

    /**
     * Button Pressed, then add the question.
     *
     * @param uri the url
     */
    public void onButtonPressed(Uri uri) {
    }

    /**
     * Called when a fragment is first attached to its context.
     *
     * @param context the Context will be attached.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    /**
     * Called when the fragment is no longer attached to its activity.
     */
    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        // TODO save current content in EditText and recover it in on Resume
    }

    private String buildAnswerURL() {
        StringBuilder sb = new StringBuilder(ADD_ANS_URL);
        try {
            sb.append("email=");
            sb.append(URLEncoder.encode(mUserEmail, "UTF-8"));
            Log.i(LOG, mUserEmail);

            sb.append("&questID=");
            sb.append(URLEncoder.encode(mQuestID, "UTF-8"));
            Log.i(LOG, mQuestID);

            String ansDetail = mAnsEditText.getText().toString();
            sb.append("&ansDetail=");
            Log.i(LOG, ansDetail);
            sb.append(URLEncoder.encode(ansDetail, "UTF-8"));
        } catch (Exception e) {
            Log.i(LOG, "Some thing wrong with the url " + e.getMessage());
            Toast.makeText(getContext(), "Something wrong with the url " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
        return sb.toString();
    }

    private void addRecentAnswer() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
            // Display User TextView
        TextView userTextView = new TextView(this.getActivity());
        params.setMargins(10, 0, 0, 5);
        userTextView.setLayoutParams(params);
        setUserTextView(mUserEmail, userTextView);
        mAnsContainer.addView(userTextView);

        // Display Answer TextView
        TextView answerTextView = new TextView(this.getActivity());
        params.setMargins(10, 0, 0, 0);
        answerTextView.setLayoutParams(params);
        answerTextView.setText(mAnsEditText.getText().toString());
        mAnsContainer.addView(answerTextView);

        // Display DiffTime TextView
        TextView timeTextView = new TextView(this.getActivity());
        params.setMargins(10, 0, 0, 5);
        timeTextView.setLayoutParams(params);
        timeTextView.setText("Just now");
        timeTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
                getResources().getDimensionPixelSize(R.dimen.text_time_size));
        mAnsContainer.addView(timeTextView);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
//    public interface OnFragmentInteractionListener {
//        void onFragmentInteraction(Uri uri);
//    }

    private class DownloadTagsTask extends AsyncTask<String, Void, String> {

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param urls The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();
                    InputStream content = urlConnection.getInputStream();
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s;
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }
                } catch (Exception e) {
                    response = "Unable to download the list of tags, Reason: " + e.getMessage();
                } finally {
                    if (urlConnection != null) urlConnection.disconnect();
                }
            }
            return response;
        }

        /**
         * It checks to see if there was a problem with the URL(Network) which is when an
         * exception is caught. It tries to call the parse Method and checks to see if it was successful.
         * If not, it displays the exception.
         *
         * @param result display the result.
         */
        @Override
        protected void onPostExecute(String result) {  // result will be exactly the response above
            // Something wrong with the network or the URL.
            if (result.startsWith("Unable to")) {
                Toast.makeText(getActivity().getApplicationContext(),
                        result, Toast.LENGTH_LONG).show();
                return;
            }
            List<String> tags = new ArrayList<>();
            result = Question.parseTagsQuestionJSON(result, tags);
            // Something wrong with the JSON returned.
            if (result != null) {
                Toast.makeText(getActivity().getApplicationContext(),
                        result, Toast.LENGTH_LONG).show();
                return;
            }
            mTags = tags;
            // Everything is good, show the list of courses.
            if (!mTags.isEmpty()) {
                setTagsView();
            }
        }
    }

    private class DownloadAnswersTask extends AsyncTask<String, Void, String> {

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param urls The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();
                    InputStream content = urlConnection.getInputStream();
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s;
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }
                } catch (Exception e) {
                    response = "Unable to download the list of answers, Reason: " + e.getMessage();
                } finally {
                    if (urlConnection != null) urlConnection.disconnect();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            if (result.startsWith("Unable to")) {
                Toast.makeText(getActivity().getApplicationContext(),
                        result, Toast.LENGTH_LONG).show();
                return;
            }
            List<Answer> ansList = new ArrayList<>();
            result = Answer.parseAnswerJSON(result, ansList);
            // Something wrong with the JSON returned.
            if (result != null) {
                Toast.makeText(getActivity().getApplicationContext(),
                        result, Toast.LENGTH_LONG).show();
                return;
            }
            mAnswersList = ansList;
            // Everything is good, show the list of courses.
            if (!mAnswersList.isEmpty()) {
                setAnsList();
            }
        }
    }

    private class AddAnswerTask extends AsyncTask<String, Void, String> {

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param urls The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();
                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s;
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    response = "Unable to add question, Reason: " + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        /**
         * It checks to see if there was a problem with the URL(Network) which is when an
         * exception is caught. It tries to call the parse Method and checks to see if it was successful.
         * If not, it displays the exception.
         *
         * @param result the string in echo from php file.
         */
        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            try {
                JSONObject jsonObject = new JSONObject(result);
                String status = (String) jsonObject.get("result");
                if (status.equals("success")) {
                    addRecentAnswer();
                    mAnsEditText.setText(null);
                    Toast.makeText(getContext(), "Answer successfully added!",
                            Toast.LENGTH_LONG).show();
                } else {
                    Log.i(LOG, jsonObject.get("error").toString());
                    Toast.makeText(getContext(), "Failed to add: "
                            + jsonObject.get("error"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                Log.i(LOG, e.getMessage());
                Toast.makeText(getContext(), "Something wrong with the data "
                        + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
