package org.menhera.spotnotes;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RemindersAdapter extends RecyclerView.Adapter<RemindersAdapter.ReminderHolder> {
    private List<ReminderItem> items;

    public static class ReminderHolder extends RecyclerView.ViewHolder {
        public LinearLayout layout;
        public ReminderHolder (LinearLayout view) {
            super (view);
            this.layout = view;
        }
    }

    public RemindersAdapter (List<ReminderItem> items) {
        this.items = items;
    }

    public void setItems (List<ReminderItem> items) {
        this.items = items;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RemindersAdapter.ReminderHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reminder_item, parent, false);

        ReminderHolder vh = new ReminderHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ReminderHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        TextView remitemTitle = holder.layout.findViewById(R.id.remitemTitle);
        TextView remitemDateTime = holder.layout.findViewById(R.id.remitemDateTime);
        TextView remitemLocation = holder.layout.findViewById(R.id.remitemLocation);
        ReminderItem item = this.items.get (position);
        remitemTitle.setText(item.getTitle());
        remitemDateTime.setText(item.getDateTime());
        remitemLocation.setText(item.getLocation());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return items.size ();
    }

}
