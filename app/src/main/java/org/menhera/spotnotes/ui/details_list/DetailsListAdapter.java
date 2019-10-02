package org.menhera.spotnotes.ui.details_list;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.menhera.spotnotes.R;
import org.menhera.spotnotes.data.Record;
import org.menhera.spotnotes.data.RecordGroup;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DetailsListAdapter extends RecyclerView.Adapter<DetailsListAdapter.RecordHolder> {
    private List<Record> records;

    public static class RecordHolder extends RecyclerView.ViewHolder {
        public LinearLayout layout;
        public RecordHolder(LinearLayout view) {
            super (view);
            this.layout = view;
        }
    }

    public DetailsListAdapter() {
        super();
        records = new ArrayList<>();
    }

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
        notifyDataSetChanged();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecordHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.record_single_item, parent, false);

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
        TextView recSingleItemTitle = holder.layout.findViewById(R.id.recSingleItemTitle);
        TextView recSingleItemStart = holder.layout.findViewById(R.id.recSingleItemStart);
        TextView recSingleItemFinished = holder.layout.findViewById(R.id.recSingleItemFinished);
        TextView recSingleItemDuration = holder.layout.findViewById(R.id.recSingleItemDuration);
        TextView recSingleItemAddress = holder.layout.findViewById(R.id.recSingleItemAddress);

        Record record = records.get(position);
        recSingleItemTitle.setText(record.title);
        String duration = formatDuration(record.duration);
        Resources resources = holder.layout.getResources();

        recSingleItemDuration.setText(String.format(Locale.getDefault(), resources.getString(R.string.format_duration), duration));

        Calendar start = Calendar.getInstance();
        start.setTimeInMillis(record.startTime);
        Calendar finished = Calendar.getInstance();
        finished.setTimeInMillis(record.finishedTime);
        recSingleItemStart.setText(String.format(Locale.getDefault(), resources.getString(R.string.format_start), DateFormat.getDateTimeInstance().format(start.getTime())));
        recSingleItemFinished.setText(String.format(Locale.getDefault(), resources.getString(R.string.format_finished), DateFormat.getDateTimeInstance().format(finished.getTime())));
        recSingleItemAddress.setText(record.address);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return records.size ();
    }

}
