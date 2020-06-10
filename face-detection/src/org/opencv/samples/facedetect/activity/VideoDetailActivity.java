package org.opencv.samples.facedetect.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bench.android.core.app.activity.BaseActivity;
import com.bench.android.core.net.imageloader.base.ImageLoaderHelper;
import com.bench.android.core.view.recyclerview.BaseAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.opencv.samples.facedetect.LogUtils;
import org.opencv.samples.facedetect.R;
import org.opencv.samples.facedetect.model.VideoDetailModel;
import org.opencv.samples.player.VideoPlayActivity;

import java.util.ArrayList;

public class VideoDetailActivity extends BaseActivity {

    private RecyclerView recyclerView;

    private ArrayList<VideoDetailModel> list;

    public static void startVideoDetailActivity(Context context, ArrayList<VideoDetailModel> list) {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra("list", list);
        intent.setClass(context, VideoDetailActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);
        list = getIntent().getParcelableArrayListExtra("list");
        initView();

    }

    @Override
    protected String getToolbarTitle() {
        return "精彩详情";
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        BaseAdapter baseAdapter = new BaseAdapter<VideoDetailModel>(R.layout.item_video_detail) {
            @Override
            public void onBindViewHolder(BaseViewHolder helper, VideoDetailModel item) {
                View view = helper.getView(R.id.imageIv);
                ImageLoaderHelper.getInstance().load(item.imgUrl) .errorResId(R.drawable.icon_logo).into(view);
            }

        };

        baseAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LogUtils.e("-----------------");
                VideoPlayActivity.start(mContext, list.get(position).videoUrl);
            }
        });

        recyclerView.setAdapter(baseAdapter);

        baseAdapter.setNewData(list);

    }
}
