package com.player.base.cover;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.kk.taurus.playerbase.receiver.BaseCover;
import com.player.base.R;
import com.player.base.play.DataInter;

/**
 * @author xingjiu
 */
public class CloseCover extends BaseCover implements View.OnClickListener {

    ImageView mCloseIcon;


    public CloseCover(Context context) {
        super(context);
    }

    @Override
    public void onReceiverBind() {
        super.onReceiverBind();
        View view = getView();
        mCloseIcon = view.findViewById(R.id.iv_close);
        mCloseIcon.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        notifyReceiverEvent(DataInter.Event.EVENT_CODE_REQUEST_CLOSE, null);
    }

    @Override
    public void onReceiverUnBind() {
        super.onReceiverUnBind();
    }

    @Override
    public View onCreateCoverView(Context context) {
        return View.inflate(context, R.layout.layout_close_cover, null);
    }

    @Override
    public void onPlayerEvent(int eventCode, Bundle bundle) {

    }

    @Override
    public void onErrorEvent(int eventCode, Bundle bundle) {

    }

    @Override
    public void onReceiverEvent(int eventCode, Bundle bundle) {

    }

    @Override
    public int getCoverLevel() {
        return levelMedium(10);
    }
}
