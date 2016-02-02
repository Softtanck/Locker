package com.softtanck.locker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.softtanck.locker.utils.LockerHelper;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Tanck";
    private LockerHelper lockerHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        lockerHelper = LockerHelper.getInstance(MainActivity.this);
        lockerHelper.startGodService();
        lockerHelper.setLockerStatus(true);
    }

    public void Enable(View view) {
        lockerHelper.setLockerStatus(false);
    }


    public void Disable(View view) {
    }


}
