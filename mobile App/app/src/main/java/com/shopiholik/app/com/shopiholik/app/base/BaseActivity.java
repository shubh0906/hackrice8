package com.shopiholik.app.com.shopiholik.app.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import butterknife.ButterKnife;

/**
 * @author agrawroh
 * @version v1.0
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected String TAG = getTAG();

    protected abstract int getLayoutId();

    protected abstract String getTAG();

    protected Context mContext;

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    @Override
    @TargetApi(21)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mContext = this;
        setContentView(getLayoutId());

        ButterKnife.bind(this);

        Window window = getWindow();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        getNecessaryData();
        initResAndListener();
    }

    protected void initResAndListener() {
        /* Implement */
    }

    protected void getNecessaryData() {
        /* Implement */
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
