package org.menhera.spotnotes.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.menhera.spotnotes.SpotNotesRepository;
import org.menhera.spotnotes.ui.app.SpotNotesApplication;

public class BootCompletedIntentReceiver extends BroadcastReceiver {
    SpotNotesApplication application;
    SpotNotesRepository repository;

    @Override
    public void onReceive (Context context, Intent intent) {
        if (!("android.intent.action.BOOT_COMPLETED".equals(intent.getAction()))) {
            return;
        }

        // write code here
        application = (SpotNotesApplication) context.getApplicationContext();
        repository = SpotNotesRepository.getInstance (context);
        Log.d(BootCompletedIntentReceiver.class.getName(), "BOOT_COMPLETED");
    }
}
