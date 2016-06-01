/*
 * TCSS 450 - Mobile App Programming
 * @author Weiwei Shi, Kyle Doan
 * @version 1.0
 */
package tcss450.uw.edu.mobileproject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import tcss450.uw.edu.mobileproject.model.Question;
import tcss450.uw.edu.mobileproject.offlineDatabase.ProjectDB;

/**
 * A fragment representing a list of Questions.
 * <p>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 * @author KyleD
 * @version May 5, 2016
 */
public class QuestionsListFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";

    public static final String QUEST_URL =
            "http://cssgate.insttech.washington.edu/~_450btm7/test.php?cmd=questions";
    private int mColumnCount = 1;
    private ProjectDB mDatabase;
    private OnListFragmentInteractionListener mListener;
    private RecyclerView mRecyclerView;
    private List<Question> mQuestsList;
    private List<Question> mDisplayList;
    private String mTagFilter;
    private String mTagAllString = getResources().getString(R.string.tag_all);

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public QuestionsListFragment() {

    }

    /**
     * Called when the activity is starting.
     *
     * @param savedInstanceState contains the data it most recently supplied.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTagFilter = mTagAllString;
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    public void filterListBasedOnTag(String tag) {
        mTagFilter = tag;
        if (tag.equals(mTagAllString)) {
            mRecyclerView.setAdapter(new MyQuestionsListRecyclerViewAdapter(mQuestsList, mListener));
        } else {
            List<Question> displayList;
            if (mDatabase == null) {
                mDatabase = new ProjectDB(getActivity());
            }
            displayList = mDatabase.getQuestionsListBasedOnTag(tag);
            mDisplayList = new ArrayList<>(displayList);
            mRecyclerView.setAdapter(new MyQuestionsListRecyclerViewAdapter(mDisplayList, mListener));
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
        View view = inflater.inflate(R.layout.fragment_questions_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            Log.i(ARG_COLUMN_COUNT, String.valueOf(mColumnCount));
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            mRecyclerView = recyclerView;
        }

        FloatingActionButton floatingActionButton = (FloatingActionButton)
                getActivity().findViewById(R.id.add_question);
        floatingActionButton.show();

        FloatingActionButton floatingActionButton2 = (FloatingActionButton)
                getActivity().findViewById(R.id.share);
        floatingActionButton2.hide();

        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            DownloadQuestionsTask task = new DownloadQuestionsTask();
            task.execute(QUEST_URL);
        } else {
            Toast.makeText(view.getContext(),
                    "No network connection available. Cannot display courses",
                    Toast.LENGTH_SHORT).show();
            if (mDatabase == null) {
                mDatabase = new ProjectDB(getActivity());
            }
            if (mQuestsList == null) {
                mQuestsList = mDatabase.getQuestionsList();
            }
            if (!mTagFilter.equals(mTagAllString)) {
                filterListBasedOnTag(mTagFilter);
            } else {
                mRecyclerView.setAdapter(new MyQuestionsListRecyclerViewAdapter(mQuestsList, mListener));
            }
//            mDatabase.closeDB();
        }
        return view;
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        if (mDisplayList != null) {
//            mRecyclerView.setAdapter(new MyQuestionsListRecyclerViewAdapter(mDisplayList, mListener));
//        } else if (mQuestsList != null && mQuestsList.size() > 0){
//            mRecyclerView.setAdapter(new MyQuestionsListRecyclerViewAdapter(mQuestsList, mListener));
//        }
//    }

    /**
     * Called when a fragment is first attached to its context.
     *
     * @param context the Context will be attached.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
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
     * Retrieve data by using web service.
     */
    private class DownloadQuestionsTask extends AsyncTask<String, Void, String> {

        private ProjectDB mDB;

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p/>
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
                    response = "Unable to download the list of questions, Reason: " + e.getMessage();
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
            List<Question> questList = new ArrayList<>();
            result = Question.parseQuestionJSON(result, questList);
            // Something wrong with the JSON returned.
            if (result != null) {
                Toast.makeText(getActivity().getApplicationContext(),
                        result, Toast.LENGTH_LONG).show();
                return;
            }
            mQuestsList = new ArrayList<>(questList);
            // Everything is good, show the list of courses.
            if (!questList.isEmpty()) {
//                mRecyclerView.setAdapter(new MyQuestionsListRecyclerViewAdapter(questList, mListener));

                if (mDB == null) {
                    mDB = new ProjectDB(getActivity());
                }

                // Delete old data so that you can refresh the local
                // database with the network data.
                mDB.deleteQuestionsTable();

                for (int i = 0; i < questList.size(); i++) {
                    Question quest = questList.get(i);
                    mDB.insertQuestion(quest.getId(),
                            quest.getUser(),
                            quest.getQuestDatePost(),
                            quest.getQuestDetail(),
                            quest.getCompany());
                }
                filterListBasedOnTag(mTagFilter);
//                mDB.closeDB();
            }
        }
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Question item);
    }
}