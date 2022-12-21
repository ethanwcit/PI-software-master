package com.group20.pi_software.ui.feed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.group20.pi_software.R;

import java.util.List;


public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {
    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView textmain,textdate ;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            textmain = itemView.findViewById(R.id.text_main);
            textdate = itemView.findViewById(R.id.text_date);
        }
    }
    // Store a member variable for the contacts
    private List<Feed_Class> mFeed_Class  ;

    // Pass in the contact array into the constructor
    public FeedAdapter(List<Feed_Class> feed_Class) {
        mFeed_Class = feed_Class;
    }
    @Override
    public FeedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View view = inflater.inflate(R.layout.feed_row_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(FeedAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        Feed_Class feed_class = mFeed_Class.get(position);
        // Set item views based on your views and data model
        TextView textView = holder.textmain;
        TextView textView2 = holder.textdate;
        textView.setText(feed_class.getMainMessage());
        textView2.setText(feed_class.getCurrTime());
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mFeed_Class.size();
    }
}