/*
 * TCSS 450 - Mobile App Programming
 * @author Weiwei Shi, Kyle Doan
 * @version 1.0
 */

package tcss450.uw.edu.mobileproject;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import java.net.URLEncoder;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QuestionAddListener} interface
 * to handle interaction events.
 * Use the {@link QuestionAddFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * @author KyleD
 * @version 1.0
 */
public class QuestionAddFragment extends Fragment {

    private final static String LOG = "QuestionAddFragment";

    private final static String QUESTION_ADD_URL =
            "http://cssgate.insttech.washington.edu/~_450btm7/addQuestion.php?";

    public static final String USER = "user", TAGS = "tags";

    private String mUserEmail = "";
    private ArrayAdapter<String> mTagsAdapter;
    private QuestionAddListener mListener;
    private EditText mQuestDetailEditText;
    private EditText mQuestCompanyEditText;
    private MultiAutoCompleteTextView mQuestTags;

    /**
     * Required empty public constructor
     */
    public QuestionAddFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QuestionAddFragment.
     */
    public static QuestionAddFragment newInstance(String param1, String param2) {
        QuestionAddFragment fragment = new QuestionAddFragment();
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
        if (getArguments() != null) {
            mUserEmail = getArguments().getString(USER);

        }
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
        View view = inflater.inflate(R.layout.fragment_question_add, container, false);

        mQuestDetailEditText = (EditText) view.findViewById(R.id.question_detail);
        mQuestCompanyEditText = (EditText) view.findViewById(R.id.question_company);
        mQuestTags = (MultiAutoCompleteTextView) view.findViewById(R.id.question_tags);
        mQuestTags.setAdapter(mTagsAdapter);
        mQuestTags.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        FloatingActionButton floatingActionButton = (FloatingActionButton)
                getActivity().findViewById(R.id.add_question);
        floatingActionButton.hide();

        Button addCourseButton = (Button) view.findViewById(R.id.add_question_button);
        addCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = buildQuestURL(v);
                mListener.addQuestion(url);
            }
        });
        return view;
    }

//    /**
//     * Set Email.
//     *
//     * @param email email
//     */
//    public void setUserEmail(String email) {
//        mUserEmail = email;
//    }

    public void setTagsAdapter(List<String> tags) {
        Log.i(LOG, "Size of tags is " + tags.size());
        if (!tags.isEmpty()) {
            mTagsAdapter = new ArrayAdapter<>(
                    getContext(),   // FIXME NullPointerException????
                    android.R.layout.simple_dropdown_item_1line,
                    tags);
        } else {
            // TODO if list is empty.
        }
    }

    /**
     * Build the Url.
     *
     * @param v the user input EditText
     * @return the Url.
     */
    private String buildQuestURL(View v) {
        StringBuilder sb = new StringBuilder(QUESTION_ADD_URL);
        try {
            sb.append("email=");
            sb.append(URLEncoder.encode(mUserEmail, "UTF-8"));
            Log.i(LOG, mUserEmail);

            String questDetail = mQuestDetailEditText.getText().toString();
            sb.append("&questDetail=");
            Log.i(LOG, questDetail);
            sb.append(URLEncoder.encode(questDetail, "UTF-8"));

            String questCompany = mQuestCompanyEditText.getText().toString();
            sb.append("&questCompany=");
            Log.i(LOG, questCompany);
            sb.append(URLEncoder.encode(questCompany, "UTF-8"));

            String tags = mQuestTags.getText().toString();
            sb.append("&tags=");
            Log.i(LOG, tags);
            sb.append(URLEncoder.encode(tags, "UTF-8"));
        } catch (Exception e) {
            Toast.makeText(v.getContext(), "Something wrong with the url " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
        return sb.toString();
    }


    /**
     * Button Pressed, then add the question.
     *
     * @param url the url
     */
    public void onButtonPressed(String url) {
        if (mListener != null) {
            mListener.addQuestion(url);
        }
    }

    /**
     * Called when a fragment is first attached to its context.
     *
     * @param context the Context will be attached.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof QuestionAddListener) {
            mListener = (QuestionAddListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement QuestionAddListener");
        }
    }

    /**
     * Called when the fragment is no longer attached to its activity.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface QuestionAddListener {
        void addQuestion(String url);
    }
}