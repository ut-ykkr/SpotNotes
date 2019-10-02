package org.menhera.spotnotes.ui.records_list;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.menhera.spotnotes.R;
import org.menhera.spotnotes.data.RecordGroup;
import org.menhera.spotnotes.ui.RecordItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RecordsAdapter extends RecyclerView.Adapter<RecordsAdapter.RecordHolder> {
    private List<RecordGroup> groups;

    public static class RecordHolder extends RecyclerView.ViewHolder {
        public LinearLayout layout;
        public RecordHolder(LinearLayout view) {
            super (view);
            this.layout = view;
        }
    }

    public RecordsAdapter () {
        super();
        groups = new ArrayList<>();
    }

    public List<RecordGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<RecordGroup> groups) {
        this.groups = groups;
        notifyDataSetChanged();
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

    private String formatDuration (long milliseconds) {
        long s = milliseconds / 1000;
        return String.format(Locale.getDefault(), "%d:%02d:%02d", s / 3600, (s % 3600) / 60, (s % 60));
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecordHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        TextView recitemTitle = holder.layout.findViewById(R.id.recitemTitle);
        TextView recitemDuration = holder.layout.findViewById(R.id.recitemDuration);
        TextView recitemTotalDuration = holder.layout.findViewById(R.id.recitemTotalDuration);

        RecordGroup group = groups.get(position);
        recitemTitle.setText(group.title);
        String avgDuration = formatDuration(group.avgDuration);
        String totalDuration = formatDuration(group.totalDuration);
        Resources resources = holder.layout.getResources();

        // todo: use String.format()
        recitemDuration.setText(resources.getString(R.string.avg_time) + avgDuration);
        recitemTotalDuration.setText(resources.getString(R.string.total_time) + totalDuration);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return groups.size ();
    }

}
