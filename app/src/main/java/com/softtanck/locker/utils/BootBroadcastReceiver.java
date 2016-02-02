package com.softtanck.locker.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Tanck on 2016/1/25.
 */
public class BootBroadcastReceiver extends BroadcastReceiver {
    static final String action_boot = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(action_boot)) {
            Intent gods = new Intent(context, GodService.class);
            context.startService(gods);
        }
    }
}
