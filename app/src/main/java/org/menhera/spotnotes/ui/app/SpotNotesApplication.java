package org.menhera.spotnotes.ui.app;

import android.app.Application;
import android.content.Context;

import org.menhera.spotnotes.ui.ReminderItem;
import org.menhera.spotnotes.SpotNotesListItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EventListener;
import java.util.List;
import java.util.WeakHashMap;

final public class SpotNotesApplication extends Application {
    AppViewModel viewModel;

    List<ReminderItem> reminderItems;
    List<ReminderItem> reminderTrashItems;

    WeakHashMap<Context, ItemsChangeListener> reminderItemsListeners;
    WeakHashMap<Context, ItemsChangeListener> reminderTrashItemsListeners;

    public interface ItemsChangeListener extends EventListener {
        public void onItemsChange (List<? extends SpotNotesListItem> items);
    }

    @Override
    public void onCreate() {
        /** Called when the Application-class is first created. */
        super.onCreate();
        viewModel = new AppViewModel(this);
        reminderItems = new ArrayList<>();
        reminderTrashItems = new ArrayList<>();
        reminderItemsListeners = new WeakHashMap<>();
        reminderTrashItemsListeners = new WeakHashMap<>();
    }

    public AppViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public void onTerminate() {
        /** This Method Called when this Application finished. */
        super.onTerminate();

    }

    public void registerReminderItemsListener (Context context, ItemsChangeListener listener) {
        reminderItemsListeners.put(context, listener);
    }

    protected void callReminderItemsListeners () {
        Collection<ItemsChangeListener> listeners = reminderItemsListeners.values();
        for (ItemsChangeListener listener: listeners) {
            listener.onItemsChange (reminderItems);
        }
    }

    public void registerReminderTrashItemsListener (Context context, ItemsChangeListener listener) {
        reminderTrashItemsListeners.put(context, listener);
    }

    protected void callReminderTrashItemsListeners () {
        Collection<ItemsChangeListener> listeners = reminderTrashItemsListeners.values();
        for (ItemsChangeListener listener: listeners) {
            listener.onItemsChange (reminderTrashItems);
        }
    }

    public int addReminderItem (ReminderItem reminderItem) {
        int index = reminderItemsSize();
        reminderItems.add(reminderItem);
        callReminderItemsListeners();
        return index;
    }

    public int trashReminderItem (int index) {
        int newIndex = reminderTrashItems.size();
        ReminderItem item = reminderItems.remove (index);
        reminderTrashItems.add(item);
        callReminderItemsListeners();
        callReminderTrashItemsListeners();
        return newIndex;
    }

    public int trashReminderItem (ReminderItem item) {
        int newIndex = reminderTrashItems.size();
        reminderItems.remove (item);
        reminderTrashItems.add(item);
        callReminderItemsListeners();
        callReminderTrashItemsListeners();
        return newIndex;
    }

    public void removeReminderTrashItem (int index) {
        reminderTrashItems.remove (index);
        callReminderTrashItemsListeners();
    }

    public void removeReminderTrashItem (ReminderItem item) {
        reminderTrashItems.remove(item);
        callReminderTrashItemsListeners();
    }

    public List<ReminderItem> getReminderItems () {
        return reminderItems;
    }

    public int reminderItemsSize() {
        return reminderItems.size();
    }

    public ReminderItem getReminderItem(int index) {
        return reminderItems.get(index);
    }

    public List<ReminderItem> getReminderTrashItems () {
        return reminderTrashItems;
    }

    public int reminderTrashItemsSize() {
        return reminderTrashItems.size();
    }

    public ReminderItem getReminderTrashItem(int index) {
        return reminderTrashItems.get(index);
    }
}
