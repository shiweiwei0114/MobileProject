/**
 * TCSS 450 - Mobile App Programming
 * May 5th, 2016
 * Weiwei Shi, Kyle Doan
 */
package tcss450.uw.edu.mobileproject;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tcss450.uw.edu.mobileproject.QuestionsListFragment.OnListFragmentInteractionListener;
import tcss450.uw.edu.mobileproject.model.Question;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Question} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * @author KyleD
 * @version 1.0
 */
public class MyQuestionsListRecyclerViewAdapter extends RecyclerView.Adapter<MyQuestionsListRecyclerViewAdapter.ViewHolder> {

    /**List of questions. */
    private final List<Question> mValues;

    /**Listener for callback. */
    private final OnListFragmentInteractionListener mListener;

    public MyQuestionsListRecyclerViewAdapter(List<Question> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_questions, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(mValues.get(position).getQuestDetail());
        holder.mDateView.setText(mValues.get(position).getQuestDatePost());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    /**
     * ViewHolder class.
     * @author KyleD, Weiwei
     * @version 1.0
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public final TextView mDateView;
        public Question mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.title);
            mDateView = (TextView) view.findViewById(R.id.question_date_post);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
