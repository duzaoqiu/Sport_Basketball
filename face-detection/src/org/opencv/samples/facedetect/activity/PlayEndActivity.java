//package org.opencv.samples.facedetect.activity;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.graphics.drawable.BitmapDrawable;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.Nullable;
//
//import com.bench.android.core.app.activity.BaseActivity;
//import com.bench.android.core.app.toast.ToastUtils;
//import com.bench.android.core.net.imageloader.base.ImageLoaderHelper;
//import com.bench.android.core.net.imageloader.base.SimpleImageView;
//import com.google.gson.Gson;
//
//import org.jcodec.common.StringUtils;
//import org.opencv.samples.facedetect.LogUtils;
//import org.opencv.samples.facedetect.R;
//import org.opencv.samples.facedetect.model.VideoPlayBackModel;
//import org.opencv.samples.facedetect.utils.AliCloudUploadHelper;
//import org.opencv.samples.facedetect.utils.BasketBallAreaUtils;
//import org.opencv.samples.facedetect.utils.CommonUtils;
//import org.opencv.samples.facedetect.utils.ImageSizeConstants;
//import org.opencv.samples.facedetect.utils.VideoProperty;
//import org.opencv.samples.facedetect.view.CircleView;
//import org.opencv.samples.facedetect.view.PointLocationScoreView;
//import org.opencv.samples.facedetect.view.PointLocationView;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Set;
//
//import static android.provider.MediaStore.Video.Thumbnails.MINI_KIND;
//
///**
// * 拍摄结束界面
// *
// * @author zhouyi
// */
//public class PlayEndActivity extends BaseActivity implements View.OnClickListener {
//
//    private PointLocationScoreView locationPointScoreView;
//
//    private SimpleImageView videoIv;
//    private ImageView mSmallVideoIv;
//    private CircleView circleAllView;
//    private TextView allRateTv;
//    private TextView allTv;
//    private CircleView circle2View;
//    private TextView twoRateTv;
//    private TextView twoTv;
//    private CircleView circle3View;
//    private TextView threeRateTv;
//    private TextView threeTv;
//    private PointLocationView pointLocationView;
//    private Button shareBtn;
//
//    private String mVideoPath;
//
//    private ArrayList<VideoPlayBackModel.FrameData> mFrameList = new ArrayList<>();
//
//    private String mHttpVideoPath;
//    private String mHttpVideoCoverPath;
//
//    private List<VideoPlayBackModel.PointLocationData> mScoreList;
//    private VideoPlayBackModel mPlayBackModel = new VideoPlayBackModel();
//
//    public static void startPlayEndActivity(Activity activity,
//                                            ArrayList<VideoPlayBackModel.FrameData> frameList,
//                                            String videoPath) {
//        Intent intent = new Intent(activity, PlayEndActivity.class);
//        intent.putParcelableArrayListExtra("FrameData", frameList);
//        intent.putExtra("videoPath", videoPath);
//
//        activity.startActivity(intent);
//    }
//
//    /**
//     * 动态页进入的界面
//     *
//     * @param activity
//     * @param modelData
//     * @param videoPath
//     */
//    public static void startPlayEndActivity(Activity activity,
//                                            String modelData,
//                                            String videoCoverUrl,
//                                            String videoPath) {
//        Intent intent = new Intent(activity, PlayEndActivity.class);
//        intent.putExtra("modelData", modelData);
//        intent.putExtra("videoCoverUrl", videoCoverUrl);
//        intent.putExtra("videoPath", videoPath);
//        activity.startActivity(intent);
//    }
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_play_end);
//        initData();
//        initView();
//        setPointLocation();
//    }
//
//    private void initData() {
//        String modelData = getIntent().getStringExtra("modelData");
//        mHttpVideoCoverPath = getIntent().getStringExtra("videoCoverUrl");
//        mHttpVideoPath = getIntent().getStringExtra("videoPath");
//        if (modelData != null) {
//            mPlayBackModel = new Gson().fromJson(modelData, VideoPlayBackModel.class);
//            List<VideoPlayBackModel.PointLocationData> data = mPlayBackModel.getData();
//            HashMap<Integer, VideoPlayBackModel.FrameData> map = mPlayBackModel.getFrameMap();
//            mScoreList = data;
//            Set<Integer> integers = map.keySet();
//            List<Integer> list = new ArrayList<>();
//            for (Integer i : integers) {
//                list.add(i);
//            }
//            Collections.sort(list);
//            for (Integer i : list) {
//                mFrameList.add(map.get(i));
//            }
//        } else {
//            mVideoPath = getIntent().getStringExtra("videoPath");
//            mFrameList = getIntent().getParcelableArrayListExtra("FrameData");
//
//        }
//    }
//
//    private void initView() {
//        if (mScoreList == null) {
//            mScoreList = BasketBallAreaUtils.getPointLocationScore(mFrameList);
//        }
//
//        //计算3分球、2分球
//        int score3Win = 0;
//        int score2Win = 0;
//        int scoreWin = 0;
//        int score3 = 0;
//        int score2 = 0;
//        //出手数
//        int shootCount = 0;
//        if (mHttpVideoCoverPath != null) {
//            shootCount = mPlayBackModel.getScoreAllShootCount();
//            score2Win = mPlayBackModel.getScore2ShootWin();
//            score3Win = mPlayBackModel.getScore3ShootWin();
//            scoreWin = mPlayBackModel.getScoreAllShootWin();
//        } else {
//            shootCount = mFrameList.size();
//            for (int i = 0; i < mScoreList.size(); i++) {
//                //3分球
//                if (i == 0 || i == 5 || i == 11 || i == 12 || i == 13) {
//                    score3Win += mScoreList.get(i).winCount;
//                    score3 += mScoreList.get(i).shootCount;
//                } else {
//                    score2 += mScoreList.get(i).shootCount;
//                    score2Win += mScoreList.get(i).winCount;
//                }
//            }
//            scoreWin = score2Win + score3Win;
//
//            mPlayBackModel.setScoreAllShootWin(scoreWin);
//            mPlayBackModel.setScoreAllShootCount(shootCount);
//
//            mPlayBackModel.setScore3ShootWin(score3Win);
//            mPlayBackModel.setScore3ShootCount(score3);
//
//            mPlayBackModel.setScore2ShootCount(score2);
//            mPlayBackModel.setScore2ShootWin(score2Win);
//
//            mPlayBackModel.setData(mScoreList);
//        }
//
//
//        locationPointScoreView = findViewById(R.id.locationPointView);
////        locationPointScoreView.setDataList(mScoreList);
//
//        //总的进球数
//        circleAllView = findViewById(R.id.circleAllView);
//        circleAllView.setCircleColor(Color.parseColor("#F7D1A5"));
//        allRateTv = findViewById(R.id.allRateTv);
//        allTv = findViewById(R.id.allTv);
//        circleAllView.setSweepAngle(CommonUtils.calculateAngle(scoreWin, shootCount));
//        allRateTv.setText(CommonUtils.calculateRate(scoreWin, shootCount));
//        allTv.setText("全部 " + scoreWin + "/" + shootCount);
//
//        //2分球数
//        circle2View = (CircleView) findViewById(R.id.circle2View);
//        circle2View.setCircleColor(Color.parseColor("#EE9790"));
//        circle2View.setSweepAngle(CommonUtils.calculateAngle(score2Win, shootCount));
//        twoRateTv = (TextView) findViewById(R.id.twoRateTv);
//        twoTv = (TextView) findViewById(R.id.twoTv);
//        circle2View.setSweepAngle(CommonUtils.calculateAngle(score2Win, shootCount));
//        twoRateTv.setText(CommonUtils.calculateRate(score2Win, shootCount));
//        twoTv.setText("2分球 " + score2Win + "/" + shootCount);
//
//        //3分球数
//        circle3View = (CircleView) findViewById(R.id.circle3View);
//        circle3View.setCircleColor(Color.parseColor("#A9ABF9"));
//        circle3View.setSweepAngle(CommonUtils.calculateAngle(score3Win, shootCount));
//        threeRateTv = (TextView) findViewById(R.id.threeRateTv);
//        threeTv = (TextView) findViewById(R.id.threeTv);
//        circle3View.setSweepAngle(CommonUtils.calculateAngle(score3Win, shootCount));
//        threeRateTv.setText((CommonUtils.calculateRate(score3Win, shootCount)));
//        threeTv.setText("3分球 " + score3Win + "/" + shootCount);
//
//        pointLocationView = (PointLocationView) findViewById(R.id.pointLocationView);
//        pointLocationView.setList(mFrameList);
//
//        shareBtn = findViewById(R.id.shareBtn);
//        shareBtn.setOnClickListener(this);
//
//        videoIv = findViewById(R.id.videoIv);
//        if (!StringUtils.isEmpty(mVideoPath)) {
//            Bitmap videoThumbnail = CommonUtils.getVideoThumbnail(mVideoPath, MINI_KIND, 1280, 720);
//            BitmapDrawable drawable = new BitmapDrawable(videoThumbnail);
//            videoIv.setBackground(drawable);
//            mSmallVideoIv = findViewById(R.id.smallVideoIv);
//            BitmapDrawable drawable1 = new BitmapDrawable(videoThumbnail);
//            mSmallVideoIv.setBackground(drawable1);
//        }
//
//        //从动态跳进来
//        if (mHttpVideoCoverPath != null) {
//            ImageLoaderHelper.getInstance().load(mHttpVideoCoverPath).into(videoIv);
//            shareBtn.setVisibility(View.GONE);
//            findViewById(R.id.highlightLayout).setVisibility(View.GONE);
//        }
//
//        videoIv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String path = null;
//                if (mVideoPath != null) {
//                    path = mVideoPath;
//                } else if (mHttpVideoPath != null) {
//                    path = mHttpVideoPath;
//                }
//                if (path != null) {
//                    //PlayBackActivity.startActivity(PlayEndActivity.this, PlayBackActivity.TYPE_DECODER_VIDEO, path, mFrameList);
//                } else {
//                    ToastUtils.show("视频不可播放");
//                }
//            }
//        });
//
//        TextView shootCountTv = (TextView) findViewById(R.id.shootCountTv);
//        TextView winCountTv = (TextView) findViewById(R.id.winCountTv);
//        TextView winRateTv = (TextView) findViewById(R.id.winRateTv);
//
//        shootCountTv.setText(shootCount + "");
//        winCountTv.setText(scoreWin + "");
//        winRateTv.setText(CommonUtils.calculateRate(scoreWin, shootCount));
//
//        final View baseProgressView = findViewById(R.id.baseProgressView);
//        final View progressView = findViewById(R.id.progressView);
//        final int finalScoreAll = scoreWin;
//        final int finalShootCount = shootCount;
//        progressView.post(new Runnable() {
//            @Override
//            public void run() {
//                int measuredWidth = baseProgressView.getMeasuredWidth();
//                int width = (int) (((float) finalScoreAll / finalShootCount) * measuredWidth);
//                Log.e("数量", measuredWidth + " ");
//                progressView.getLayoutParams().width = width;
//                progressView.getLayoutParams().height = progressView.getLayoutParams().height;
//                progressView.setLayoutParams(progressView.getLayoutParams());
//            }
//        });
//
//        TextView titleTv = (TextView) findViewById(R.id.titleTv);
//        titleTv.setText("详情");
//        View viewById = findViewById(R.id.backIv);
//        viewById.setVisibility(View.VISIBLE);
//        viewById.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//    }
//
//    private void setPointLocation() {
//
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.shareBtn: {
//                mPlayBackModel.setPointWidth(ImageSizeConstants.TOP_RIGHT_IMAGE_WIDTH);
//                mPlayBackModel.setPointHeight(ImageSizeConstants.TOP_RIGHT_IMAGE_HEIGHT);
//
//                VideoProperty videoFrameRate = CommonUtils.getVideoFrameRate(mVideoPath);
//                mPlayBackModel.setVideoDuration((int) videoFrameRate.getDuration());
//                HashMap<Integer, VideoPlayBackModel.FrameData> map = new HashMap<>();
//                for (VideoPlayBackModel.FrameData f : mFrameList) {
//                    map.put(f.frameCount, f);
//                }
//                mPlayBackModel.setFrameMap(map);
//                String s = new Gson().toJson(mPlayBackModel, VideoPlayBackModel.class);
//                showProgressDialog("正在上传中");
//                LogUtils.e("视频路径" + mVideoPath);
//                new AliCloudUploadHelper().uploadHttp(s, mVideoPath, new AliCloudUploadHelper.OnUploadSuccessListener() {
//                    @Override
//                    public void onSuccess() {
//                        hideProgressDialog();
//                        finish();
//                    }
//
//                    @Override
//                    public void onFailed(String msg) {
//                        hideProgressDialog();
//                    }
//                });
//            }
//            break;
//            default:
//                break;
//        }
//    }
//}
