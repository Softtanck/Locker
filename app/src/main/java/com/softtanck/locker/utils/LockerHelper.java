package com.softtanck.locker.utils;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PowerManager;

import com.softtanck.locker.LockerActivity;

/**
 * Created by Tanck on 2016/1/25.
 */
public class LockerHelper {

    private static LockerHelper instance;

    private static final String DEFALUT_LOCK_NAME = "DEFALUT_LOCK_NAME";
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private Context context;

    private LockerHelper(Context context) {
        this.context = context;
        sp = context.getSharedPreferences(DEFALUT_LOCK_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public static LockerHelper getInstance(Context context) {
        if (null == instance) {
            synchronized (LockerHelper.class) {
                if (null == instance)
                    instance = new LockerHelper(context);
            }
        }
        return instance;
    }


    public boolean getLockerStatus() {
        return sp.getBoolean(DEFALUT_LOCK_NAME, false);
    }


    public void setLockerStatus(boolean lockerStatus) {
        editor.putBoolean(DEFALUT_LOCK_NAME, lockerStatus);
        editor.commit();
    }

    public void wakeUpScreen(final long de) {
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(de);
                    KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
                    KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
                    kl.disableKeyguard();
                    PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
                    PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
                    wl.acquire();
                    wl.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void startGodService() {
        Intent god = new Intent(context, GodService.class);
        context.startService(god);
    }


    public void startLock() {
        Intent lock = new Intent(context, LockerActivity.class);
        context.startActivity(lock);
    }
}
