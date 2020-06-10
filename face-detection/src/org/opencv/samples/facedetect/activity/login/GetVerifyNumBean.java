package org.opencv.samples.facedetect.activity.login;

import com.bench.android.core.util.bean.BaseResponse;

public class GetVerifyNumBean extends BaseResponse {
    /**
     * smsId : 05b3f703-e71e-41f4-a008-786c897cd4e7
     * waitNextPrepareSeconds : 60
     */

    private String smsId;
    private long waitNextPrepareSeconds;

    public String getSmsId() {
        return smsId;
    }

    public void setSmsId(String smsId) {
        this.smsId = smsId;
    }

    public long getWaitNextPrepareSeconds() {
        return waitNextPrepareSeconds;
    }

    public void setWaitNextPrepareSeconds(long waitNextPrepareSeconds) {
        this.waitNextPrepareSeconds = waitNextPrepareSeconds;
    }
}
