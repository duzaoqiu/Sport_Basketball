package com.bench.android.core.net.domain.base;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.library.R;
import com.bench.android.core.app.activity.BaseActivity;
import com.bench.android.core.app.titlebar.TitleBar;


public class ShowErrorDetailMessageActivity extends BaseActivity {

    public static void startShowErrorDetailMessageActivity(Context context, String msg) {
        Intent intent = new Intent(context, ShowErrorDetailMessageActivity.class);
        intent.putExtra("msg", msg);
        context.startActivity(intent);
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity_show_error_detail_message);
        new TitleBar("错误信息")//标题
                .setShowBackIcon(false)//显示返回键
                .build(iTitleBar);

        final String msg = getIntent().getStringExtra("msg");
        ((TextView) findViewById(R.id.tv)).setText(msg);
        findViewById(R.id.copyBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyMessageToClipboard(msg);
            }
        });
    }

    private void copyMessageToClipboard(String message) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(message);
    }
}
