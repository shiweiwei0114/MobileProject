/*
 * TCSS 450 - Mobile App Programming
 * @author Weiwei Shi, Kyle Doan
 * @version 1.1
 */
package tcss450.uw.edu.mobileproject;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A short self-introduction of us.
 *
 * @author Weiwei Shi
 * @version May 29, 2016
 */
public class AboutUsFragment extends Fragment {

    public AboutUsFragment() {
        // Required empty public constructor
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
        FloatingActionButton floatingActionButton = (FloatingActionButton)
                getActivity().findViewById(R.id.add_question);
        floatingActionButton.hide();

        FloatingActionButton shareActionButton = (FloatingActionButton)
                getActivity().findViewById(R.id.share);
        shareActionButton.hide();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_us, container, false);
    }

}
