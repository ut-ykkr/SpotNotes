package org.menhera.spotnotes;

import android.app.Application;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

final public class SpotNotesApplication extends Application {
    List<ReminderItem> reminderItems;
    List<ReminderItem> reminderTrashItems;

    WeakHashMap<ItemsChangeListener, Object> reminderItemsListeners;
    WeakHashMap<ItemsChangeListener, Object> reminderTrashItemsListeners;

    public interface ItemsChangeListener extends EventListener {
        public void onItemsChange (List<? extends SpotNotesListItem> items);
    }

    @Override
    public void onCreate() {
        /** Called when the Application-class is first created. */
        super.onCreate();
        reminderItems = new ArrayList<>();
        reminderTrashItems = new ArrayList<>();
        reminderItemsListeners = new WeakHashMap<>();
        reminderTrashItemsListeners = new WeakHashMap<>();
    }

    @Override
    public void onTerminate() {
        /** This Method Called when this Application finished. */
        super.onTerminate();

    }

    public void registerReminderItemsListener (ItemsChangeListener listener) {
        reminderItemsListeners.put(listener, null);
    }

    protected void callReminderItemsListeners () {
        Set<ItemsChangeListener> listeners = reminderItemsListeners.keySet();
        for (ItemsChangeListener listener: listeners) {
            listener.onItemsChange (reminderItems);
        }
    }

    public void registerReminderTrashItemsListener (ItemsChangeListener listener) {
        reminderTrashItemsListeners.put(listener, null);
    }

    protected void callReminderTrashItemsListeners () {
        Set<ItemsChangeListener> listeners = reminderTrashItemsListeners.keySet();
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
