package com.bench.android.core.app.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * 对Fragment进行切面
 *
 * @author zhouyi
 */
public class PointCutFragment extends Fragment {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       // PointCutManager.getInstance().executeAdvice(PointCutConstants.POINTCUT_FRAGMENT_ON_ATTACH, this, new Object[]{context});

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       // PointCutManager.getInstance().executeAdvice(PointCutConstants.POINTCUT_FRAGMENT_ON_CREATE_VIEW, this, new Object[]{inflater, container, savedInstanceState});
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // PointCutManager.getInstance().executeAdvice(PointCutConstants.POINTCUT_FRAGMENT_ON_CREATE, this, new Object[]{savedInstanceState});
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       // PointCutManager.getInstance().executeAdvice(PointCutConstants.POINTCUT_FRAGMENT_ON_ACTIVITY_CREATED, this, new Object[]{savedInstanceState});
    }

    @Override
    public void onStart() {
        super.onStart();
       // PointCutManager.getInstance().executeAdvice(PointCutConstants.POINTCUT_FRAGMENT_ON_START, this, null);
    }

    @Override
    public void onResume() {
        super.onResume();
       // PointCutManager.getInstance().executeAdvice(PointCutConstants.POINTCUT_FRAGMENT_ON_RESUME, this, null);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       // PointCutManager.getInstance().executeAdvice(PointCutConstants.POINTCUT_FRAGMENT_ON_VIEW_CREATED, this, new Object[]{view, savedInstanceState});

    }

    @Override
    public void onPause() {
        super.onPause();
       // PointCutManager.getInstance().executeAdvice(PointCutConstants.POINTCUT_FRAGMENT_ON_PAUSE, this, null);

    }

    @Override
    public void onStop() {
        super.onStop();
      //  PointCutManager.getInstance().executeAdvice(PointCutConstants.POINTCUT_FRAGMENT_ON_STOP, this, null);

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
       // PointCutManager.getInstance().executeAdvice(PointCutConstants.POINTCUT_FRAGMENT_ON_SAVE_INSTANCE_STATE, this, new Object[]{outState});

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
       // PointCutManager.getInstance().executeAdvice(PointCutConstants.POINTCUT_FRAGMENT_ON_VIEW_STATE_RESTORED, this, new Object[]{savedInstanceState});

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
       // PointCutManager.getInstance().executeAdvice(PointCutConstants.POINTCUT_FRAGMENT_ON_REQUEST_PERMISSION_RESULT, this, new Object[]{requestCode, permissions, grantResults});

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       // PointCutManager.getInstance().executeAdvice(PointCutConstants.POINTCUT_FRAGMENT_ON_ACTIVITY_RESULT, this, new Object[]{requestCode, resultCode, data});

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
       // PointCutManager.getInstance().executeAdvice(PointCutConstants.POINTCUT_FRAGMENT_ON_DESTROY_VIEW, this, null);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       // PointCutManager.getInstance().executeAdvice(PointCutConstants.POINTCUT_FRAGMENT_ON_DESTROY, this, null);

    }

    @Override
    public void onDetach() {
        super.onDetach();
       // PointCutManager.getInstance().executeAdvice(PointCutConstants.POINTCUT_FRAGMENT_ON_DETACH, this, null);
    }


}
