package org.opencv.samples.facedetect.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.bench.android.core.app.activity.BaseActivity;
import com.bench.android.core.app.permission.AsyncPermission;
import com.bench.android.core.app.permission.info.Permission;
import com.bench.android.core.app.permission.listener.OnAsyncPermissionDeniedListener;
import com.bench.android.core.app.permission.listener.OnAsyncPermissionGrantedListener;
import com.bench.android.core.app.toast.ToastUtils;
import com.google.zxing.client.android.CaptureActivity;

import org.opencv.samples.facedetect.R;
import org.opencv.samples.view.CustomTabHost;

import java.util.ArrayList;
import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;


public class MainActivity extends BaseActivity {

    private TabWidget tabs;
    private CustomTabHost tabhost;
    private int[] ivTabs;
    private String[] tvTabs;

    public static void startMainActivity(Activity activity) {
        activity.startActivity(new Intent(activity, MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabhost = findViewById(android.R.id.tabhost);
        tabs = findViewById(android.R.id.tabs);

        List<Class> fragmentList = new ArrayList<>();
        fragmentList.add(DynamicFragment.class);
        fragmentList.add(SportFragment.class);
        fragmentList.add(MeFragment.class);


        tabhost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        tabhost.getTabWidget().setDividerDrawable(null);

        ivTabs = new int[]{R.drawable.selector_1, R.drawable.selector_2,  R.drawable.selector_4};
        tvTabs = new String[]{"动态", "运动",  "我"};

        for (int i = 0; i < ivTabs.length; i++) {
            TabHost.TabSpec tabSpec = tabhost.newTabSpec(i + "").setIndicator(getIndicatorView(i));
            tabhost.addTab(tabSpec, fragmentList.get(i), null);
        }

        tabhost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                Log.e("tab", s);
                if ("1".equals(s)) {
                    //FdActivity.startFdActivity(MainActivity.this);
                   // startActivity(new Intent(MainActivity.this,DetectorTestActivity.class));
                    return;
                }
                //tabChange(s);
            }
        });

        AsyncPermission.with(this).request(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE).onDenied(new OnAsyncPermissionDeniedListener() {
            @Override
            public void onResult(List<Permission> permissions) {
                ToastUtils.show("请给权限");
            }
        }).onAllGranted(new OnAsyncPermissionGrantedListener() {
            @Override
            public void onResult(List<Permission> permissions) {

            }
        });

        findViewById(R.id.sportLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toScan();
            }
        });
    }


    private void toScan(){
        Intent intent = new Intent(this, ScanActivity.class);
        startActivity(intent);
    }

    private View getIndicatorView(int i) {
        View view = LayoutInflater.from(this).inflate(R.layout.main_tab_item, null, false);
        ImageView ivTab = view.findViewById(R.id.ivTab);
        TextView tvTab = view.findViewById(R.id.tvTab);
        ivTab.setImageResource(ivTabs[i]);
        tvTab.setText(tvTabs[i]);
        return view;
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void askReadPermission() {

    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    public void onCameraDenied() {
        Toast.makeText(this, "请添加权限", Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    public void onCameraNeverAskAgain() {
        toScan();
    }


}
