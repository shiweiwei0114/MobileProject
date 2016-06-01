/*
 * TCSS 450 - Mobile App Programming
 * @author Weiwei Shi, Kyle Doan
 * @version 1.0
 */

package tcss450.uw.edu.mobileproject;

import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import tcss450.uw.edu.mobileproject.QuestionsListFragment.OnListFragmentInteractionListener;
import tcss450.uw.edu.mobileproject.model.Question;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Question} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 *
 * @author KyleD
 * @version 1.0
 */
public class MyQuestionsListRecyclerViewAdapter extends RecyclerView.Adapter<MyQuestionsListRecyclerViewAdapter.ViewHolder> {

    /**
     * List of questions.
     */
    private final List<Question> mValues;
    // mValues display all the list
    // TODO have another alter list to display some specific list.
    /**
     * Listener for callback.
     */
    private final OnListFragmentInteractionListener mListener;

    public MyQuestionsListRecyclerViewAdapter(List<Question> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    /**
     * Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param position The view type of the new View.
     * @return call viewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_questions, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(mValues.get(position).getQuestDetail());
        setDatePostTextView(mValues.get(position).getQuestDatePost(), holder.mDateView);
//        holder.mDateView.setText(mValues.get(position).getQuestDatePost());

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
//        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, // DIP : Device Indepentdent pixel
//                getResources().getDimensionPixelSize(R.dimen.text_time_size));
    }

    /**
     * Returns the total number of items in the data set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return mValues.size();
    }

    /**
     * ViewHolder class.
     *
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

        /**
         * toString.
         *
         * @return a string.
         */
        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
