package com.softtanck.locker.utils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.softtanck.locker.ScreenListener;

import java.util.Calendar;

/**
 * Created by Tanck on 2016/1/25.
 */
public class GodService extends Service implements ScreenListener.ScreenStateListener, SensorEventListener {
    private ScreenListener listener;

    private static final String TAG = "Tanck";
    private LockerHelper helper;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private int mX, mY, mZ;
    private long lasttimestamp = 0;
    Calendar mCalendar;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        helper = LockerHelper.getInstance(getBaseContext());
        listener = new ScreenListener(this);
        listener.begin(this);


        PowerManager manager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock mWakeLock = manager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);// CPU保存运行
        mWakeLock.acquire();// 屏幕熄后，CPU继续运行

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);// TYPE_GRAVITY
        if (null == mSensorManager) {
            Log.d(TAG, "deveice not support SensorManager");
        }
        // 参数三，检测的精准度
        mSensorManager.registerListener(this, mSensor,
                SensorManager.SENSOR_DELAY_NORMAL);// SENSOR_DELAY_GAM
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        listener.unregisterListener();
    }


    @Override
    public void onScreenOn() {
        Log.d(TAG, "onScreenOn: ");
    }

    @Override
    public void onScreenOff() {
        Log.d(TAG, "onScreenOff: ");
        mSensorManager.unregisterListener(this);
        // 参数三，检测的精准度
        mSensorManager.registerListener(this, mSensor,
                SensorManager.SENSOR_DELAY_NORMAL);// SENSOR_DELAY_GAM
//        boolean status = helper.getLockerStatus();
//        if (status) {
//            helper.startLock();
//            helper.wakeUpScreen(1000);
//        }
    }

    @Override
    public void onUserPresent() {
        Log.d(TAG, "onUserPresent: ");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor == null) {
            return;
        }

        Log.d(TAG, "onSensorChanged: ");
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            int x = (int) event.values[0];
            int y = (int) event.values[1];
            int z = (int) event.values[2];
            mCalendar = Calendar.getInstance();
            long stamp = mCalendar.getTimeInMillis() / 1000l;// 1393844912

            int second = mCalendar.get(Calendar.SECOND);// 53

            int px = Math.abs(mX - x);
            int py = Math.abs(mY - y);
            int pz = Math.abs(mZ - z);
            Log.d(TAG, "pX:" + px + "  pY:" + py + "  pZ:" + pz + "    stamp:"
                    + stamp + "  second:" + second);
            int maxvalue = getMaxValue(px, py, pz);
            if (maxvalue > 2 && (stamp - lasttimestamp) > 30) {
                lasttimestamp = stamp;
                Log.d(TAG, " sensor isMoveorchanged....");
            }

            mX = x;
            mY = y;
            mZ = z;
        }
    }

    /**
     * 获取一个最大值
     *
     * @param px
     * @param py
     * @param pz
     * @return
     */
    public int getMaxValue(int px, int py, int pz) {
        int max = 0;
        if (px > py && px > pz) {
            max = px;
        } else if (py > px && py > pz) {
            max = py;
        } else if (pz > px && pz > py) {
            max = pz;
        }

        return max;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
