/*
 * TCSS 450 - Mobile App Programming
 * @author Weiwei Shi, Kyle Doan
 * @version 1.0
 */
package tcss450.uw.edu.mobileproject;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    public static String QUEST_SELECTED = "";

    private TextView mQuestDetailTextView;
    private TextView mQuestUserPostTextView;
    private TextView mQuestDatePostTextView;
    private TextView mQuestCompanyTextView;


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
    // TODO: Rename and change types and number of parameters
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
            updateView((Question) args.getSerializable(QUEST_SELECTED));
        }
    }

    /**
     * Update the question.
     * @param quest the question be sent.
     */
    public void updateView(Question quest) {
        if (quest != null) {
            mQuestDetailTextView.setText(quest.getQuestDetail());
            mQuestUserPostTextView.setText(quest.getUser());
            mQuestDatePostTextView.setText(quest.getQuestDatePost());
            mQuestCompanyTextView.setText(quest.getCompany());
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
        View view = inflater.inflate(R.layout.fragment_question_post, container, false);
        mQuestDetailTextView = (TextView) view.findViewById(R.id.question_detail);
        mQuestUserPostTextView = (TextView) view.findViewById(R.id.question_user_post);
        mQuestDatePostTextView = (TextView) view.findViewById(R.id.question_date_post);
        mQuestCompanyTextView = (TextView) view.findViewById(R.id.question_company);
        // Inflate the layout for this fragment

        FloatingActionButton floatingActionButton = (FloatingActionButton)
                getActivity().findViewById(R.id.add_question);
        floatingActionButton.show();
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
}
