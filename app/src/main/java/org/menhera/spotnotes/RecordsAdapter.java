package org.menhera.spotnotes;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecordsAdapter extends RecyclerView.Adapter<RecordsAdapter.RecordHolder> {
    private List<RecordItem> items;

    public static class RecordHolder extends RecyclerView.ViewHolder {
        public LinearLayout layout;
        public RecordHolder(LinearLayout view) {
            super (view);
            this.layout = view;
        }
    }

    public RecordsAdapter(List<RecordItem> items) {
        this.items = items;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecordHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.record_item, parent, false);

        RecordHolder vh = new RecordHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecordHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        TextView recitemTitle = holder.layout.findViewById(R.id.recitemTitle);
        TextView recitemDuration = holder.layout.findViewById(R.id.recitemDuration);
        RecordItem item = this.items.get (position);
        recitemTitle.setText(item.getTitle());
        recitemDuration.setText(item.getAvgDuration());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return items.size ();
    }

}
