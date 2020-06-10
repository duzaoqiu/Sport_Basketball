package org.opencv.samples.facedetect.activity;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bench.android.core.app.fragment.BaseFragment;
import com.bench.android.core.app.toast.ToastUtils;
import com.bench.android.core.net.http.RequestFormBody;
import com.bench.android.core.net.http.WrapperHttpHelper;
import com.bench.android.core.net.http.base.RequestContainer;
import com.bench.android.core.net.imageloader.base.ImageLoaderHelper;
import com.bench.android.core.net.imageloader.base.RoundRadiusType;
import com.bench.android.core.net.imageloader.base.SimpleImageView;
import com.bench.android.core.view.recyclerview.BaseAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.samples.facedetect.LogUtils;
import org.opencv.samples.facedetect.R;
import org.opencv.samples.facedetect.activity.login.LoginHelper;
import org.opencv.samples.facedetect.bean.ShootCountStatisticsBean;
import org.opencv.samples.facedetect.bean.StarInfoBean;
import org.opencv.samples.facedetect.model.VideoDetailModel;
import org.opencv.samples.facedetect.utils.UrlEnum;

import java.util.List;

public class MeFragment extends BaseFragment implements View.OnClickListener {

    private RecyclerView recyclerView;
    private WrapperHttpHelper mHttpRequestHelper = new WrapperHttpHelper(this);
    private  BaseAdapter baseAdapter;
    private int clickPosition = -1;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_me_new;
    }

    @Override
    protected void onCreateView() {
        initView();
        getStarList();
    }

    private void initView() {
        recyclerView =  rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(mActivity,3));
        baseAdapter = new BaseAdapter<StarInfoBean.StarInfo>(R.layout.item_star_show) {
            @Override
            public void onBindViewHolder(BaseViewHolder helper, StarInfoBean.StarInfo item) {
                SimpleImageView view = helper.getView(R.id.imageView);
                ImageLoaderHelper.getInstance()
                        .load(item.getAvatar())
                        .centerCrop(true)
                        .roundCircleRadius(RoundRadiusType.CORNER_ALL,30)
                        .errorResId(R.drawable.icon_logo)
                        .into(view);

                TextView tv =helper.getView(R.id.nameTv);
                tv.setText(item.getName());

                if(item.isSelected){
                    tv.setTextColor(Color.parseColor("#ffffff"));
                    helper.getView(R.id.layout).setBackgroundResource(R.drawable.shape_all_radius_small_corner_orange);
                }else{
                    tv.setTextColor(Color.parseColor("#919191"));
                    helper.getView(R.id.layout).setBackgroundResource(R.drawable.shape_all_radius_small_corner_grey);
                }
            }
        };

        baseAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                StarInfoBean.StarInfo starInfo = (StarInfoBean.StarInfo) baseAdapter.getData().get(position);
                modifyStar(starInfo.getId()+"");
                clickPosition = position;

            }
        });

        recyclerView.setAdapter(baseAdapter);

        TextView tv = rootView.findViewById(R.id.userNameTv);
        if(LoginHelper.getInstance().loginInfo!=null){
            tv.setText(LoginHelper.getInstance().loginInfo.data.user.name);
        }


        rootView.findViewById(R.id.setIv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity,SetActivity.class));
            }
        });

    }

    /**
     * 获取投篮总数汇总
     */
    private void getStarList() {
        RequestFormBody body = new RequestFormBody(UrlEnum.GET_STAR_LIST);
        body.setGenericClaz(StarInfoBean.class);
        body.setRequestUrl("/star/list");
        body.setUsePost(false);
        body.setUseJsonRequest(true);
        mHttpRequestHelper.startRequest(body);
    }

    /**
     * 修改球星
     * /user/edit
     * favoriteStarId
     * name
     */
    private void modifyStar(String favoriteStarId){
        RequestFormBody body = new RequestFormBody(UrlEnum.MODIFY_STAR);
        body.put("favoriteStarId",favoriteStarId);
        body.setRequestUrl("/user/edit");
        body.setUsePost(true);
        body.setUseJsonRequest(true);
        mHttpRequestHelper.startRequest(body);
    }


    @Override
    public void onClick(View v) {

    }


    @Override
    public void onSuccess(RequestContainer request, Object o) throws JSONException {
        super.onSuccess(request, o);
        UrlEnum requestEnum = (UrlEnum) request.getRequestEnum();
        switch (requestEnum) {
            case GET_STAR_LIST: {
                List<StarInfoBean.StarInfo> data = ((StarInfoBean) o).data;
                if(LoginHelper.getInstance().loginInfo!=null){
                    for (int i = 0; i <data.size() ; i++) {
                        if(LoginHelper.getInstance().loginInfo.data.user.favoriteStarId==data.get(i).getId()){
                            data.get(i).isSelected = true;
                            break;
                        }
                    }
                }
                baseAdapter.setNewData(((StarInfoBean) o).data);
            }
            break;
            case MODIFY_STAR: {
                ToastUtils.show("选择成功");
                JSONObject jsonObject = (JSONObject) o;
                long str = ((JSONObject)jsonObject.get("data")).getLong("favoriteStarId");
                if(LoginHelper.getInstance().loginInfo!=null){
                    LoginHelper.getInstance().loginInfo.data.user.favoriteStarId=str;
                }

                for (int i = 0; i <  baseAdapter.getData().size(); i++) {
                    StarInfoBean.StarInfo starInfo = (StarInfoBean.StarInfo) baseAdapter.getData().get(i);
                    starInfo.isSelected = false;
                }
                ((StarInfoBean.StarInfo) baseAdapter.getData().get(clickPosition)).isSelected = true;

                baseAdapter.notifyDataSetChanged();
            }
            break;
            default:
                break;
        }
    }


}
