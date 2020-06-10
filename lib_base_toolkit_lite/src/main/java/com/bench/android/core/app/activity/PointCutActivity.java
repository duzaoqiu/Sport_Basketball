package com.bench.android.core.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 对Activity进行切面
 *
 * @author zhouyi
 */
public class PointCutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //PointCutManager.getInstance().executeAdvice(PointCutConstants.POINTCUT_ACTIVITY_ON_CREATE, this, new Object[]{savedInstanceState});
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
       // PointCutManager.getInstance().executeAdvice(PointCutConstants.POINTCUT_ACTIVITY_SET_CONTENT_VIEW_INT, this, new Object[]{layoutResID});
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
       // PointCutManager.getInstance().executeAdvice(PointCutConstants.POINTCUT_ACTIVITY_SET_CONTENT_VIEW_VIEW, this, new Object[]{view});
    }

    @Override
    protected void onStart() {
        super.onStart();
      //  PointCutManager.getInstance().executeAdvice(PointCutConstants.POINTCUT_ACTIVITY_ON_START, this, null);
    }

    @Override
    protected void onResume() {
        super.onResume();
       // PointCutManager.getInstance().executeAdvice(PointCutConstants.POINTCUT_ACTIVITY_ON_RESUME, this, null);

    }

    @Override
    protected void onPause() {
        super.onPause();
      //  PointCutManager.getInstance().executeAdvice(PointCutConstants.POINTCUT_ACTIVITY_ON_PAUSE, this, null);

    }

    @Override
    protected void onStop() {
        super.onStop();
        //PointCutManager.getInstance().executeAdvice(PointCutConstants.POINTCUT_ACTIVITY_ON_STOP, this, null);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // PointCutManager.getInstance().executeAdvice(PointCutConstants.POINTCUT_ACTIVITY_ON_DESTROY, this, null);

    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
      //  PointCutManager.getInstance().executeAdvice(PointCutConstants.POINTCUT_ACTIVITY_ON_USER_LEAVE_HINT, this, null);

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
      //  PointCutManager.getInstance().executeAdvice(PointCutConstants.POINTCUT_ACTIVITY_ON_LOW_MEMORY, this, null);

    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
      //  PointCutManager.getInstance().executeAdvice(PointCutConstants.POINTCUT_ACTIVITY_START_ACTIVITY, this, new Object[]{intent});

    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
      //  PointCutManager.getInstance().executeAdvice(PointCutConstants.POINTCUT_ACTIVITY_START_ACTIVITY_FOR_RESULT, this, new Object[]{intent, requestCode});

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
     //   PointCutManager.getInstance().executeAdvice(PointCutConstants.POINTCUT_ACTIVITY_ON_REQUEST_PERMISSION_RESULT, this, new Object[]{requestCode, permissions, grantResults});

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
      //  PointCutManager.getInstance().executeAdvice(PointCutConstants.POINTCUT_ACTIVITY_ON_ACTIVITY_RESULT, this, new Object[]{requestCode, resultCode, data});

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
       // PointCutManager.getInstance().executeAdvice(PointCutConstants.POINTCUT_ACTIVITY_ON_WINDOW_FOCUS_CHANGED, this, new Object[]{hasFocus});

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
      //  PointCutManager.getInstance().executeAdvice(PointCutConstants.POINTCUT_ACTIVITY_ON_SAVE_INSTANCE_STATE, this, new Object[]{outState});

    }
}
