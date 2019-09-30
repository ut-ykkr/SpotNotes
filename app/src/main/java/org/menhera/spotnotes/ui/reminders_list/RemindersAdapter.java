package org.menhera.spotnotes.ui.reminders_list;

import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.menhera.spotnotes.R;
import org.menhera.spotnotes.data.Reminder;

import java.security.InvalidParameterException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class RemindersAdapter extends RecyclerView.Adapter<RemindersAdapter.ReminderHolder> {
    private List<Reminder> reminders;

    private String[] radiusLabels;
    private int[] radiusValues;
    private String[] inOutlabels;
    private String[] dayLabels;

    public static class ReminderHolder extends RecyclerView.ViewHolder {
        public LinearLayout layout;
        public ReminderHolder (LinearLayout view) {
            super (view);
            this.layout = view;
        }
    }

    public RemindersAdapter () {
        this.reminders = new ArrayList<>();
    }

    public RemindersAdapter (List<Reminder> reminders) {
        this.reminders = reminders;
    }

    public List<Reminder> getReminders() {
        return reminders;
    }

    public void setReminders(List<Reminder> reminders) {
        this.reminders = reminders;
        this.notifyDataSetChanged();
        Log.d(RemindersAdapter.class.getName(), "reminders size: " + reminders.size());
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RemindersAdapter.ReminderHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reminder_item, parent, false);

        ReminderHolder vh = new ReminderHolder(v);

        Resources res = parent.getContext().getResources();
        radiusLabels = res.getStringArray(R.array.distances_array);
        radiusValues = res.getIntArray(R.array.distances_values);
        inOutlabels = res.getStringArray(R.array.in_out_array);
        dayLabels = res.getStringArray(R.array.days);
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
        Reminder reminder = this.reminders.get (position);
        remitemTitle.setText(reminder.title);
        int radiusIndex = Collections.singletonList(radiusValues).indexOf(reminder.radius);
        if (radiusIndex < 0) {
            radiusIndex = 0;
        }
        String radiusLabel = radiusLabels[radiusIndex];
        String inOutLabel = inOutlabels[reminder.inOut == Reminder.InOut.IN ? 0 : 1];
        String location = reminder.address + "\n" + radiusLabel + "\n" + inOutLabel;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(reminder.targetBaseTime);
        DateFormat dateFormat;
        String dateTime = "";
        switch (reminder.repeat) {
            case REPEAT_NONE:
                dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
                break;

            case REPEAT_DAY:
                dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
                break;

            case REPEAT_WEEK:
                int day = calendar.get(Calendar.DAY_OF_WEEK);
                dateTime += dayLabels[day];
                dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
                break;

            case REPEAT_MONTH:
                dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
                break;

            case REPEAT_YEAR:
                dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
                break;

            default:
                throw new InvalidParameterException("Invalid repetition");
        }

        dateTime += dateFormat.format(calendar.getTime());
        remitemDateTime.setText(dateTime);
        remitemLocation.setText(location);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return reminders.size ();
    }

}
