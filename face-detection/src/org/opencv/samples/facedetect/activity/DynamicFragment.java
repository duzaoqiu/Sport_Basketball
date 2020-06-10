package org.opencv.samples.facedetect.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;

import com.bench.android.core.app.fragment.BaseFragment;
import com.bench.android.core.net.http.RequestFormBody;
import com.bench.android.core.net.http.WrapperHttpHelper;
import com.bench.android.core.net.http.base.RequestContainer;
import com.bench.android.core.net.imageloader.base.ImageLoaderHelper;
import com.bench.android.core.util.TimeUtils;
import com.bench.android.core.view.recyclerview.BaseAdapter;
import com.bench.android.core.view.recyclerview.ListController;
import com.bench.android.core.view.recyclerview.PullRecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.samples.facedetect.R;
import org.opencv.samples.facedetect.activity.login.LoginHelper;
import org.opencv.samples.facedetect.bean.DynamicBean;
import org.opencv.samples.facedetect.dialog.PointDescDialog;
import org.opencv.samples.facedetect.model.DynamicModel;
import org.opencv.samples.facedetect.model.VideoDetailModel;
import org.opencv.samples.facedetect.model.VideoPlayBackModel;
import org.opencv.samples.facedetect.utils.CommonUtils;
import org.opencv.samples.facedetect.utils.UrlEnum;
import org.opencv.samples.facedetect.view.PointLocationScoreView;
import org.opencv.samples.view.DateSelectView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 动态界面
 *
 * @author zhouyi
 */
public class DynamicFragment extends BaseFragment {

    private TextView titleTv,dateTv;
    private PullRecyclerView listView;

    private ListController listController;
    private MyAdapter mAdapter;

    private WrapperHttpHelper httpHelper = new WrapperHttpHelper(this);
    private PointDescDialog pointDescDialog = null;
    private View headerView;
    private String startDate = "2020-06-03";


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void onCreateView() {
        initView(rootView);
        dynamicQuery(startDate, startDate);
    }

    private void dynamicQuery(String startTime, String endTime) {
        RequestFormBody body = new RequestFormBody(UrlEnum.TRAIN_RECORD_QUERY);
        body.put("startTime", startTime+" 00:00:00");
        body.put("endTime", endTime+" 23:59:59");
        body.setUseJsonRequest(true);
        body.setRequestUrl("/action/list");
        body.setGenericClaz(DynamicBean.class);
        httpHelper.startRequest(body);
    }

    private void initView(View inflate) {
        mAdapter = new MyAdapter(R.layout.item_main_new);

        headerView = getLayoutInflater().inflate(R.layout.layout_main_header_view, null);

        mAdapter.addHeaderView(headerView);
        mAdapter.addFooterView(getLayoutInflater().inflate(R.layout.layout_footer, null));
        dateTv = inflate.findViewById(R.id.dateTv);
        titleTv = inflate.findViewById(R.id.titleTv);
        listView = inflate.findViewById(R.id.listView);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider_horizontal_line));
        listView.addItemDecoration(dividerItemDecoration);
        listController = new ListController(listView, mAdapter, null);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            }
        });

        View logoIv = rootView.findViewById(R.id.userLogoIv);

        String logoUrl = LoginHelper.getInstance().loginInfo.data.user.avatar;
        ImageLoaderHelper.getInstance().load(logoUrl).circle(true).into(logoIv);


        rootView.findViewById(R.id.scanIv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, ScanActivity.class);
                startActivity(intent);
            }
        });

        rootView.findViewById(R.id.searchLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal;
                if (StringUtils.isBlank(startDate)) {
                    cal = Calendar.getInstance(Locale.CHINA);
                } else {
                    cal = TimeUtils.stringToCalendar(startDate, TimeUtils.DATEFORMAT);
                }

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(),
                        android.R.style.Theme_DeviceDefault_Light_Dialog_Alert,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                startDate = formatDay(year, month, dayOfMonth);
                                dateTv.setText(startDate);
                                dynamicQuery(startDate,startDate);
                            }
                        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();
            }
        });

        startDate = dateTv.getText().toString();

        TextView tv = rootView.findViewById(R.id.userNameTv);
        if(LoginHelper.getInstance().loginInfo!=null){
            tv.setText(LoginHelper.getInstance().loginInfo.data.user.name);
        }
    }

    @Override
    public void onSuccess(RequestContainer request, Object o) throws JSONException {
        DynamicBean bean = (DynamicBean) o;
        mAdapter.setNewData(bean.data.list);
        setNumLayout(bean.data.stats);
        //listController.loadComplete(list, bean.getPaginator());
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        super.onFailed(request, jsonObject, netFailed);
        listController.loadComplete(null, null);
    }


    class MyAdapter extends BaseAdapter<DynamicBean.ListBean> {

        public MyAdapter(int layoutId) {
            super(layoutId);
        }

        @Override
        public void onBindViewHolder(BaseViewHolder helper, final DynamicBean.ListBean data) {
            Date date = TimeUtils.stringToDate(data.createTime, TimeUtils.LONG_DATEFORMAT);
            String hourMinute = TimeUtils.getHourMinute(date);
            helper.setText(R.id.timeTv, hourMinute);

            helper.setText(R.id.dateTv,TimeUtils.getDate(date));
            ImageLoaderHelper.getInstance().load(data.orgVideos.size()>0?data.orgVideos.get(0).imgUrl:"")
                    .errorResId(R.drawable.icon_logo)
                    .roundCircleRadius(30)
                    .into(helper.getView(R.id.imageIv));
        }

        @Override
        public void setOnItemClick(View v, int position) {
            super.setOnItemClick(v, position);
            DynamicBean.ListBean l = mData.get(position);
            ArrayList<VideoDetailModel> videos = new ArrayList<>();
            videos.addAll(l.orgVideos);
            videos.addAll(l.analyzeVideos);
            VideoDetailActivity.startVideoDetailActivity(mActivity, videos);
        }
    }

    private void setNumLayout(DynamicBean.StatsBean data) {
        TextView allRateTv = headerView.findViewById(R.id.allRateTv);
        TextView allTv = headerView.findViewById(R.id.allTv);
        TextView twoRateTv = headerView.findViewById(R.id.twoRateTv);
        TextView twoTv = headerView.findViewById(R.id.twoTv);
        TextView threeRateTv = headerView.findViewById(R.id.threeRateTv);
        TextView threeTv = headerView.findViewById(R.id.threeTv);

        if (data.total!= null) {
            allRateTv.setText(data.total.sucRate*100+"%");
            allTv.setText(data.total.sucCount+"/"+data.total.tryCount);
        }else{

        }
        if(data.point2!=null){
            twoRateTv.setText(data.point2.sucRate*100+"%");
            twoTv.setText(data.point2.sucCount+"/"+data.point2.tryCount);

        }else{
            twoRateTv.setText("-");
            twoTv.setText("-");
        }
        if(data.point3!=null){
            threeRateTv.setText(data.point3.sucRate*100+"%");
            threeTv.setText(data.point3.sucCount+"/"+data.point3.tryCount);
        } else {
            threeRateTv.setText("-");
            threeTv.setText("-");
        }

        PointLocationScoreView view = headerView.findViewById(R.id.tableIv);
        if (data.detail == null) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
            view.setDataList(data.detail);
        }

        headerView.findViewById(R.id.showDescTv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pointDescDialog == null) {
                    pointDescDialog = new PointDescDialog(getActivity());
                }
                pointDescDialog.show();
            }
        });
    }

    private String formatDay(int year, int month, int day) {
        StringBuilder sb = new StringBuilder();
        sb.append(year);
        sb.append("-");
        if (month + 1 < 10) {
            sb.append("0");
        }
        sb.append(month + 1);
        sb.append("-");
        if (day < 10) {
            sb.append("0");
        }
        sb.append((day));
        return sb.toString();
    }
}
