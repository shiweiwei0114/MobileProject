/**
 * TCSS 450 - Mobile App Programming
 * May 5th, 2016
 * Weiwei Shi, Kyle Doan
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
 * @author KyleD, Weiwei
 * @version 1.0
 */
public class QuestionPostFragment extends Fragment {

    public static String QUEST_SELECTED = "";

    private TextView mQuestDetailTextView;
    private TextView mQuestUserPostTextView;
    private TextView mQuestDatePostTextView;
    private TextView mQuestCompanyTextView;

//    private OnFragmentInteractionListener mListener;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // During startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.
//        Bundle args = getArguments();
//        if (args != null) {
//            // Set article based on argument passed in
//        }
    }

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

    public void updateView(Question quest) {
        if (quest != null) {
            mQuestDetailTextView.setText(quest.getQuestDetail());
            mQuestUserPostTextView.setText(quest.getUser());
            mQuestDatePostTextView.setText(quest.getQuestDatePost());
            mQuestCompanyTextView.setText(quest.getCompany());
        }
    }

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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement QuestionAddListener");
//        }
    }

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
//        // TODO: Update argument type and name
//        // TODO: decide what to do when user touch the question post
//        void onFragmentInteraction(Uri uri);
//    }
}
